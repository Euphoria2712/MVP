package intent_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import intent_service.client.AiServiceClient;
import intent_service.client.FreshmartServiceClient;
import intent_service.client.LocationServiceClient;
import intent_service.client.RecipeServiceClient;
import intent_service.dto.IntentRequest;
import intent_service.dto.IntentResponse;
import intent_service.dto.PriceData;
import intent_service.dto.RecipeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntentService {

    private final AiServiceClient        aiServiceClient;
    private final RecipeServiceClient    recipeServiceClient;
    private final FreshmartServiceClient freshmartServiceClient;
    private final LocationServiceClient  locationServiceClient;
    
    // Configuramos el ObjectMapper para ignorar propiedades desconocidas (como "tipo") al mapear a RecipeData
    private final ObjectMapper           objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public IntentResponse process(IntentRequest request, String userId) {

        // 1. llama al ai-service para detectar intención y obtener respuesta
        Map<String, String> body = Map.of(
            "mensaje", request.getMensaje()
        );

        Map<String, Object> aiResponse = aiServiceClient.chat(
            userId, "Usuario", body
        );

        String intentType     = (String) aiResponse.get("intentType");
        String respuesta      = (String) aiResponse.get("respuesta");
        String conversacionId = (String) aiResponse.get("conversacionId");
        Object datos          = aiResponse.get("datos");

        log.info("Intent detectado: {}", intentType);

        // 2. según el intent, enriquece la respuesta
        return switch (intentType) {
            case "recipe" -> processRecipe(
                datos, respuesta, conversacionId, request
            );
            case "search" -> processSearch(
                datos, respuesta, conversacionId, request
            );
            default -> IntentResponse.builder()
                .intentType("chat")
                .respuesta(respuesta)
                .conversacionId(conversacionId)
                .build();
        };
    }

    private IntentResponse processRecipe(Object datos, String respuesta,
                                         String conversacionId,
                                         IntentRequest request) {
        RecipeData recipeData = null;
        List<PriceData> precios = new ArrayList<>();

        try {
            if (datos != null) {
                // Convierte el objeto genérico a JSON String
                String jsonStr = objectMapper.writeValueAsString(datos);
                
                // Leemos como JsonNode para inspeccionar si la IA nos envió una raíz envuelta
                JsonNode jsonNode = objectMapper.readTree(jsonStr);
                
                // DESEMPAQUETADO INTELIGENTE: Si viene envuelto en un nodo "recipe" o "receta", nos metemos dentro
                if (jsonNode.has("recipe") && jsonNode.get("recipe").isObject()) {
                    jsonNode = jsonNode.get("recipe");
                } else if (jsonNode.has("receta") && jsonNode.get("receta").isObject()) {
                    jsonNode = jsonNode.get("receta");
                }

                // Deserializamos el JsonNode limpio al DTO de RecipeData (ahora tolerando campos extra como "tipo")
                recipeData = objectMapper.treeToValue(jsonNode, RecipeData.class);

                // Busca precios de cada ingrediente de forma inteligente
                if (recipeData != null && recipeData.getIngredientes() != null) {
                    for (RecipeData.Ingredient ing : recipeData.getIngredientes()) {
                        String ingredienteOriginal = ing.getNombre();
                        // Simplificamos el nombre para asegurar coincidencia en las tiendas ficticias
                        String ingredienteSimplificado = simplificarIngrediente(ingredienteOriginal);
                        
                        try {
                            log.info("Buscando precios para ingrediente: '{}' (simplificado de '{}')", 
                                     ingredienteSimplificado, ingredienteOriginal);
                                     
                            List<PriceData> p = freshmartServiceClient.getPrices(ingredienteSimplificado);
                            
                            // Si no encontró con el simplificado, intentamos con el original por si acaso
                            if ((p == null || p.isEmpty()) && !ingredienteOriginal.equalsIgnoreCase(ingredienteSimplificado)) {
                                p = freshmartServiceClient.getPrices(ingredienteOriginal);
                            }
                            
                            if (p != null) {
                                precios.addAll(p);
                            }
                        } catch (Exception e) {
                            log.warn("Sin precio para el ingrediente: {} (Error: {})", ingredienteOriginal, e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error crítico procesando receta: {}", e.getMessage(), e);
        }

        // Obtener ubicaciones cercanas
        Object ubicacion = null;
        if (request.getUserLat() != null && request.getUserLng() != null) {
            try {
                ubicacion = locationServiceClient.getAllNearby(
                    request.getUserLat(), request.getUserLng(),
                    request.getRadiusKm() != null ? request.getRadiusKm() : 10.0
                );
            } catch (Exception e) {
                log.warn("Sin ubicación: {}", e.getMessage());
            }
        }

        return IntentResponse.builder()
            .intentType("recipe")
            .respuesta(respuesta)
            .conversacionId(conversacionId)
            .receta(recipeData)
            .precios(precios)
            .ubicacion(ubicacion)
            .build();
    }

    private IntentResponse processSearch(Object datos, String respuesta,
                                         String conversacionId,
                                         IntentRequest request) {
        List<PriceData> precios = new ArrayList<>();

        try {
            Map<String, Object> datosMap = objectMapper.convertValue(datos, Map.class);
            String producto = (String) datosMap.get("producto");

            if (producto != null) {
                String productoSimplificado = simplificarIngrediente(producto);
                precios = freshmartServiceClient.getPrices(productoSimplificado);
                
                if (precios == null || precios.isEmpty()) {
                    precios = freshmartServiceClient.getPrices(producto);
                }
            }
        } catch (Exception e) {
            log.error("Error procesando búsqueda: {}", e.getMessage());
        }

        return IntentResponse.builder()
            .intentType("search")
            .respuesta(respuesta)
            .conversacionId(conversacionId)
            .precios(precios)
            .build();
    }

    /**
     * Simplifica nombres complejos de ingredientes para aumentar un 1000% las coincidencias
     * con los nombres simples de las tiendas ficticias de Freshmart.
     */
    private String simplificarIngrediente(String nombre) {
        if (nombre == null) return "";
        String clean = nombre.toLowerCase().trim();
        
        // Removemos acentos para facilitar búsquedas en la BD ficticia
        clean = clean.replace("á", "a")
                     .replace("é", "e")
                     .replace("í", "i")
                     .replace("ó", "o")
                     .replace("ú", "u");
        
        // Reglas de mapeo rápido a términos genéricos
        if (clean.contains("mantequilla")) return "mantequilla";
        if (clean.contains("azucar"))      return "azucar";
        if (clean.contains("limon"))       return "limon";
        if (clean.contains("huevo"))       return "huevo";
        if (clean.contains("galleta"))     return "galletas";
        if (clean.contains("leche"))       return "leche";
        if (clean.contains("harina"))      return "harina";
        if (clean.contains("crema"))       return "crema";
        if (clean.contains("aceite"))      return "aceite";
        if (clean.contains("sal"))         return "sal";
        if (clean.contains("vainilla"))    return "vainilla";
        
        // Si contiene la estructura "X de Y" (ej: "jugo de limón", "ralladura de naranja")
        if (clean.contains(" de ")) {
            String[] parts = clean.split(" de ");
            if (parts.length > 1) {
                String parteEspecifica = parts[1].trim();
                // Si la segunda parte es un ingrediente base común, lo priorizamos
                if (parteEspecifica.equals("limon") || parteEspecifica.equals("naranja") || 
                    parteEspecifica.equals("vainilla") || parteEspecifica.equals("coco")) {
                    return parteEspecifica;
                }
                return parts[0].trim();
            }
        }
        
        return clean;
    }
}