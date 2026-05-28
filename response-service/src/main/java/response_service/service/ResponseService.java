package response_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import response_service.client.IntentServiceClient;
import response_service.dto.ChatRequest;
import response_service.dto.FinalResponse;
import response_service.dto.PriceCard;
import response_service.dto.RecipeCard;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseService {

    private final IntentServiceClient intentServiceClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FinalResponse process(String userId, ChatRequest request) {

        // 1. llama al intent-service con todos los datos
        Map<String, Object> intentRequest = new HashMap<>();
        intentRequest.put("mensaje",   request.getMensaje());
        intentRequest.put("userLat",   request.getUserLat());
        intentRequest.put("userLng",   request.getUserLng());
        intentRequest.put("radiusKm",  request.getRadiusKm() != null
                                       ? request.getRadiusKm() : 10.0);

        Map<String, Object> intentResponse =
            intentServiceClient.process(userId, intentRequest);

        String tipo           = (String) intentResponse.get("intentType");
        String mensaje        = (String) intentResponse.get("respuesta");
        String conversacionId = (String) intentResponse.get("conversacionId");
        Object sucursales     = intentResponse.get("ubicacion");

        // 2. construye la respuesta según el tipo
        return switch (tipo != null ? tipo : "chat") {
            case "recipe" -> buildRecipeResponse(
                intentResponse, mensaje, conversacionId, sucursales
            );
            case "search" -> buildSearchResponse(
                intentResponse, mensaje, conversacionId
            );
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
        RecipeCard receta     = null;
        List<PriceCard> precios = new ArrayList<>();
        int costoTotal        = 0;
        Map<String, Integer>  costosPorTienda = new HashMap<>();

        try {
            // extrae la receta
            Object recetaObj = intentResponse.get("receta");
            if (recetaObj != null) {
                receta = objectMapper.convertValue(recetaObj, RecipeCard.class);
            }

            // extrae y transforma los precios
            Object preciosObj = intentResponse.get("precios");
            if (preciosObj != null) {
                List<Map<String, Object>> preciosRaw =
                    (List<Map<String, Object>>) preciosObj;

                for (Map<String, Object> p : preciosRaw) {
                    List<Map<String, Object>> comparacionRaw =
                        (List<Map<String, Object>>) p.get("precios");

                    if (comparacionRaw == null) continue;

                    List<PriceCard.StorePrice> comparacion =
                        comparacionRaw.stream()
                            .map(s -> new PriceCard.StorePrice(
                                (String)  s.get("storeName"),
                                (Integer) s.get("precio"),
                                (Boolean) s.get("disponible")
                            ))
                            .collect(Collectors.toList());

                    // calcula costo por tienda
                    for (PriceCard.StorePrice sp : comparacion) {
                        costosPorTienda.merge(
                            sp.getTienda(), sp.getPrecio(), Integer::sum
                        );
                    }

                    Map<String, Object> masBarato =
                        (Map<String, Object>) p.get("masBarato");

                    PriceCard card = PriceCard.builder()
                        .producto((String) p.get("producto"))
                        .unidad((String) p.get("unidad"))
                        .masBaratoTienda(masBarato != null
                            ? (String) masBarato.get("storeName") : "")
                        .masBaratoPrecio(masBarato != null
                            ? (Integer) masBarato.get("precio") : 0)
                        .comparacion(comparacion)
                        .build();

                    precios.add(card);

                    if (masBarato != null && masBarato.get("precio") != null) {
                        costoTotal += (Integer) masBarato.get("precio");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error construyendo respuesta de receta: {}",
                      e.getMessage());
        }

        // tienda con menor costo total
        String tiendaRecomendada = costosPorTienda.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("SimerMart");

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
                List<Map<String, Object>> preciosRaw =
                    (List<Map<String, Object>>) preciosObj;

                for (Map<String, Object> p : preciosRaw) {
                    List<Map<String, Object>> comparacionRaw =
                        (List<Map<String, Object>>) p.get("precios");

                    if (comparacionRaw == null) continue;

                    List<PriceCard.StorePrice> comparacion =
                        comparacionRaw.stream()
                            .map(s -> new PriceCard.StorePrice(
                                (String)  s.get("storeName"),
                                (Integer) s.get("precio"),
                                (Boolean) s.get("disponible")
                            ))
                            .collect(Collectors.toList());

                    Map<String, Object> masBarato =
                        (Map<String, Object>) p.get("masBarato");

                    precios.add(PriceCard.builder()
                        .producto((String) p.get("producto"))
                        .unidad((String) p.get("unidad"))
                        .masBaratoTienda(masBarato != null
                            ? (String) masBarato.get("storeName") : "")
                        .masBaratoPrecio(masBarato != null
                            ? (Integer) masBarato.get("precio") : 0)
                        .comparacion(comparacion)
                        .build());
                }
            }
        } catch (Exception e) {
            log.error("Error construyendo respuesta de búsqueda: {}",
                      e.getMessage());
        }

        return FinalResponse.builder()
            .tipo("search")
            .mensaje(mensaje)
            .conversacionId(conversacionId)
            .precios(precios)
            .build();
    }
}