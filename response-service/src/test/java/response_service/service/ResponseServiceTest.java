package response_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import response_service.client.IntentServiceClient;
import response_service.dto.ChatRequest;
import response_service.dto.FinalResponse;
import response_service.dto.PriceCard;
import response_service.dto.RecipeCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseServiceTest {

        @Mock
        private IntentServiceClient intentServiceClient;

        @Mock
        private ObjectMapper objectMapper;

        @InjectMocks
        private ResponseService responseService;

        @Test
        void process_shouldReturnChatResponseWhenIntentIsChat() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Hola");
                request.setUserLat(-33.0);
                request.setUserLng(-71.0);
                request.setRadiusKm(5.0);

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("intentType", "chat");
                intentResponse.put("respuesta", "Hola Catalina");
                intentResponse.put("conversacionId", "conv-1");

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                FinalResponse response = responseService.process("user-1", request);

                assertEquals("chat", response.getTipo());
                assertEquals("Hola Catalina", response.getMensaje());
                assertEquals("conv-1", response.getConversacionId());

                verify(intentServiceClient).process(eq("user-1"), anyMap());
        }

        @Test
        void process_shouldReturnChatResponseWhenIntentTypeIsNull() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Mensaje normal");

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("respuesta", "Respuesta normal");
                intentResponse.put("conversacionId", "conv-2");

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                FinalResponse response = responseService.process("user-1", request);

                assertEquals("chat", response.getTipo());
                assertEquals("Respuesta normal", response.getMensaje());
                assertEquals("conv-2", response.getConversacionId());
        }

        @Test
        void process_shouldUseDefaultRadiusWhenRadiusIsNull() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Buscar arroz");
                request.setUserLat(-33.0);
                request.setUserLng(-71.0);
                request.setRadiusKm(null);

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("intentType", "chat");
                intentResponse.put("respuesta", "Ok");
                intentResponse.put("conversacionId", "conv-3");

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                responseService.process("user-1", request);

                verify(intentServiceClient).process(eq("user-1"), argThat(map -> map.get("radiusKm").equals(10.0)));
        }

        @Test
        void process_shouldReturnSearchResponseWithPrices() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Busca leche");

                Object preciosObj = List.of(Map.of("producto", "Leche"));

                List<PriceCard> precios = List.of(new PriceCard());

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("intentType", "search");
                intentResponse.put("respuesta", "Encontré precios");
                intentResponse.put("conversacionId", "conv-4");
                intentResponse.put("precios", preciosObj);

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                doReturn(precios)
                                .when(objectMapper)
                                .convertValue(eq(preciosObj), any(TypeReference.class));

                FinalResponse response = responseService.process("user-1", request);

                assertEquals("search", response.getTipo());
                assertEquals("Encontré precios", response.getMensaje());
                assertEquals("conv-4", response.getConversacionId());
                assertEquals(1, response.getPrecios().size());
        }

        @Test
        void process_shouldReturnSearchResponseWithEmptyPricesWhenMapperFails() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Busca pan");

                Object preciosObj = List.of(Map.of("producto", "Pan"));

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("intentType", "search");
                intentResponse.put("respuesta", "Error controlado");
                intentResponse.put("conversacionId", "conv-5");
                intentResponse.put("precios", preciosObj);

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                doThrow(new IllegalArgumentException("Error mapper"))
                                .when(objectMapper)
                                .convertValue(eq(preciosObj), any(TypeReference.class));

                FinalResponse response = responseService.process("user-1", request);

                assertEquals("search", response.getTipo());
                assertEquals("Error controlado", response.getMensaje());
                assertNotNull(response.getPrecios());
                assertEquals(0, response.getPrecios().size());
        }

        @Test
        void process_shouldReturnRecipeResponse() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Dame una receta");

                Object recetaObj = Map.of("nombre", "Arroz con pollo");
                Object preciosObj = List.of(Map.of("producto", "Arroz"));
                Object ubicacionObj = List.of(Map.of("storeName", "Lider"));

                RecipeCard recipeCard = new RecipeCard();
                List<PriceCard> precios = List.of(new PriceCard());

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("intentType", "recipe");
                intentResponse.put("respuesta", "Aquí tienes una receta");
                intentResponse.put("conversacionId", "conv-6");
                intentResponse.put("receta", recetaObj);
                intentResponse.put("precios", preciosObj);
                intentResponse.put("ubicacion", ubicacionObj);

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                when(objectMapper.convertValue(recetaObj, RecipeCard.class))
                                .thenReturn(recipeCard);

                doReturn(precios)
                                .when(objectMapper)
                                .convertValue(eq(preciosObj), any(TypeReference.class));

                FinalResponse response = responseService.process("user-1", request);

                assertEquals("recipe", response.getTipo());
                assertEquals("Aquí tienes una receta", response.getMensaje());
                assertEquals("conv-6", response.getConversacionId());
                assertNotNull(response.getReceta());
                assertEquals(1, response.getPrecios().size());
                assertEquals(ubicacionObj, response.getSucursales());
                assertEquals("Lider", response.getTiendaRecomendada());
        }

        @Test
        void process_shouldReturnRecipeResponseWhenMapperFails() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Receta con error");

                Object recetaObj = Map.of("nombre", "Error");

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("intentType", "recipe");
                intentResponse.put("respuesta", "Respuesta receta");
                intentResponse.put("conversacionId", "conv-7");
                intentResponse.put("receta", recetaObj);

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                when(objectMapper.convertValue(recetaObj, RecipeCard.class))
                                .thenThrow(new IllegalArgumentException("Error mapper"));

                FinalResponse response = responseService.process("user-1", request);

                assertEquals("recipe", response.getTipo());
                assertEquals("Respuesta receta", response.getMensaje());
                assertEquals("conv-7", response.getConversacionId());
                assertNull(response.getReceta());
                assertNotNull(response.getPrecios());
                assertEquals(0, response.getPrecios().size());
                assertEquals("Lider", response.getTiendaRecomendada());
        }

        @Test
        void process_shouldCalculateStoreTotalsAndCheapestStore() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Dame una receta");

                PriceCard.StorePrice lider = new PriceCard.StorePrice();
                lider.setStoreName("Lider");
                lider.setPrecio(1000);

                PriceCard.StorePrice jumbo = new PriceCard.StorePrice();
                jumbo.setStoreName("Jumbo");
                jumbo.setPrecio(1500);

                PriceCard priceCard = PriceCard.builder()
                                .producto("Arroz")
                                .masBarato(lider)
                                .comparacion(List.of(lider, jumbo))
                                .build();

                RecipeCard recipeCard = new RecipeCard();

                Object recetaObj = Map.of("nombre", "Arroz");
                Object preciosObj = List.of(Map.of("producto", "Arroz"));

                Map<String, Object> intentResponse = new HashMap<>();
                intentResponse.put("intentType", "recipe");
                intentResponse.put("respuesta", "Receta encontrada");
                intentResponse.put("conversacionId", "conv-100");
                intentResponse.put("receta", recetaObj);
                intentResponse.put("precios", preciosObj);

                when(intentServiceClient.process(eq("user-1"), anyMap()))
                                .thenReturn(intentResponse);

                when(objectMapper.convertValue(recetaObj, RecipeCard.class))
                                .thenReturn(recipeCard);

                doReturn(List.of(priceCard))
                                .when(objectMapper)
                                .convertValue(eq(preciosObj), any(TypeReference.class));

                FinalResponse response = responseService.process("user-1", request);

                assertEquals("recipe", response.getTipo());

                assertEquals(1000, response.getCostoEstimado());

                assertEquals("Lider", response.getTiendaRecomendada());

                assertEquals(
                                Integer.valueOf(1000),
                                response.getPrecios().get(0).getMasBaratoPrecio());

                assertEquals(
                                "Lider",
                                response.getPrecios().get(0).getMasBaratoTienda());
        }
}