package response_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import response_service.client.IntentServiceClient;
import response_service.dto.*;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseService {

    private final IntentServiceClient intentServiceClient;
    private final ObjectMapper objectMapper;

    public FinalResponse process(String userId, ChatRequest request) {
        // Preparar el request para el intent-service
        Map<String, Object> intentRequest = new HashMap<>();
        intentRequest.put("mensaje", request.getMensaje());
        intentRequest.put("userLat", request.getUserLat());
        intentRequest.put("userLng", request.getUserLng());
        intentRequest.put("radiusKm", request.getRadiusKm() != null ? request.getRadiusKm() : 10.0);

        // Llamada al microservicio de inteligencia
        Map<String, Object> intentResponse = intentServiceClient.process(userId, intentRequest);

        String tipo = (String) intentResponse.get("intentType");
        String mensaje = (String) intentResponse.get("respuesta");
        String conversacionId = (String) intentResponse.get("conversacionId");
        Object sucursales = intentResponse.get("ubicacion");

        return switch (tipo != null ? tipo : "chat") {
            case "recipe" -> buildRecipeResponse(intentResponse, mensaje, conversacionId, sucursales);
            case "search" -> buildSearchResponse(intentResponse, mensaje, conversacionId);
            default -> FinalResponse.builder()
                    .tipo("chat")
                    .mensaje(mensaje)
                    .conversacionId(conversacionId)
                    .build();
        };
    }

    private FinalResponse buildRecipeResponse(Map<String, Object> intentResponse,
                                             String mensaje,
                                             String conversacionId,
                                             Object sucursales) {
        RecipeCard receta = null;
        List<PriceCard> precios = new ArrayList<>();
        int costoTotal = 0;
        Map<String, Integer> costosPorTienda = new HashMap<>();

        try {
            // 1. Mapeo de Receta (usando la llave "receta" que viene del intent)
            Object recetaObj = intentResponse.get("receta");
            if (recetaObj != null) {
                receta = objectMapper.convertValue(recetaObj, RecipeCard.class);
            }

            // 2. Mapeo de Precios
            Object preciosObj = intentResponse.get("precios");
            if (preciosObj != null) {
                List<PriceCard> preciosData = objectMapper.convertValue(preciosObj, 
                        new TypeReference<List<PriceCard>>() {});

                for (PriceCard p : preciosData) {
                    precios.add(p);

                    // Usar el objeto masBarato para el cálculo
                    if (p.getMasBarato() != null && p.getMasBarato().getPrecio() != null) {
                        costoTotal += p.getMasBarato().getPrecio();
                        // Seteamos los campos planos para el FinalResponse si los necesitas
                        p.setMasBaratoPrecio(p.getMasBarato().getPrecio());
                        p.setMasBaratoTienda(p.getMasBarato().getStoreName());
                    }

                    // Calcular totales por tienda para la recomendación
                    if (p.getComparacion() != null) {
                        for (PriceCard.StorePrice sp : p.getComparacion()) {
                            if (sp.getPrecio() != null && sp.getStoreName() != null) {
                                costosPorTienda.merge(sp.getStoreName(), sp.getPrecio(), Integer::sum);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error procesando receta o precios: {}", e.getMessage());
        }

        // 3. Determinar tienda recomendada (el total más bajo)
        String tiendaRecomendada = costosPorTienda.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Lider"); 

        return FinalResponse.builder()
                .tipo("recipe")
                .mensaje(mensaje)
                .conversacionId(conversacionId)
                .receta(receta)
                .precios(precios)
                .sucursales(sucursales)
                .costoEstimado(costoTotal)
                .tiendaRecomendada(tiendaRecomendada)
                .build();
    }

    private FinalResponse buildSearchResponse(Map<String, Object> intentResponse,
                                             String mensaje,
                                             String conversacionId) {
        List<PriceCard> precios = new ArrayList<>();
        try {
            Object preciosObj = intentResponse.get("precios");
            if (preciosObj != null) {
                precios = objectMapper.convertValue(preciosObj, 
                        new TypeReference<List<PriceCard>>() {});
            }
        } catch (Exception e) {
            log.error("Error en búsqueda simple: {}", e.getMessage());
        }

        return FinalResponse.builder()
                .tipo("search")
                .mensaje(mensaje)
                .conversacionId(conversacionId)
                .precios(precios)
                .build();
    }
}