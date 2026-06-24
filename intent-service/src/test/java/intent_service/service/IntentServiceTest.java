package intent_service.service;

import intent_service.client.AiServiceClient;
import intent_service.client.FreshmartServiceClient;
import intent_service.client.LocationServiceClient;
import intent_service.client.RecipeServiceClient;
import intent_service.dto.IntentRequest;
import intent_service.dto.IntentResponse;
import intent_service.dto.PriceData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntentServiceTest {

        @Mock
        private AiServiceClient aiServiceClient;

        @Mock
        private RecipeServiceClient recipeServiceClient;

        @Mock
        private FreshmartServiceClient freshmartServiceClient;

        @Mock
        private LocationServiceClient locationServiceClient;

        @InjectMocks
        private IntentService intentService;

        @Test
        void process_shouldReturnChatResponse() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("hola");

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "chat");
                aiResponse.put("respuesta", "Hola usuario");
                aiResponse.put("conversacionId", "conv-1");

                when(aiServiceClient.chat(eq("user-1"), eq("Usuario"), anyMap()))
                                .thenReturn(aiResponse);

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("chat", response.getIntentType());
                assertEquals("Hola usuario", response.getRespuesta());
                assertEquals("conv-1", response.getConversacionId());

                verify(aiServiceClient).chat(eq("user-1"), eq("Usuario"), anyMap());
        }

        @Test
        void process_shouldReturnSearchResponse() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("precio leche");

                Map<String, Object> datos = new HashMap<>();
                datos.put("producto", "leche");

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "search");
                aiResponse.put("respuesta", "Encontrado");
                aiResponse.put("conversacionId", "conv-2");
                aiResponse.put("datos", datos);

                PriceData price = new PriceData();

                when(aiServiceClient.chat(eq("user-1"), eq("Usuario"), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(List.of(price));

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("search", response.getIntentType());
                assertEquals("Encontrado", response.getRespuesta());
                assertEquals("conv-2", response.getConversacionId());
                assertNotNull(response.getPrecios());
                assertEquals(1, response.getPrecios().size());
        }

        @Test
        void processSearch_shouldUseOriginalProductWhenSimplifiedReturnsEmpty() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("buscar jugo de pera");

                Map<String, Object> datos = new HashMap<>();
                datos.put("producto", "jugo de pera");

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "search");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-search");
                aiResponse.put("datos", datos);

                PriceData price = new PriceData();

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices("jugo"))
                                .thenReturn(new ArrayList<>());

                when(freshmartServiceClient.getPrices("jugo de pera"))
                                .thenReturn(List.of(price));

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("search", response.getIntentType());
                assertEquals(1, response.getPrecios().size());

                verify(freshmartServiceClient).getPrices("jugo");
                verify(freshmartServiceClient).getPrices("jugo de pera");
        }

        @Test
        void process_shouldHandleSearchException() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("buscar");

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "search");
                aiResponse.put("respuesta", "Busqueda");
                aiResponse.put("conversacionId", "conv-6");
                aiResponse.put("datos", "dato_invalido");

                when(aiServiceClient.chat(eq("user-1"), eq("Usuario"), anyMap()))
                                .thenReturn(aiResponse);

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("search", response.getIntentType());
                assertNotNull(response.getPrecios());
        }

        @Test
        void process_shouldReturnRecipeResponse() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("quiero una receta");
                request.setUserLat(-33.0);
                request.setUserLng(-71.0);
                request.setRadiusKm(5.0);

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "leche");
                ingrediente.put("cantidad", "1");
                ingrediente.put("unidad", "litro");

                Map<String, Object> receta = new HashMap<>();
                receta.put("receta", "Panqueques");
                receta.put("ingredientes", List.of(ingrediente));

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "Receta encontrada");
                aiResponse.put("conversacionId", "conv-3");
                aiResponse.put("datos", receta);

                when(aiServiceClient.chat(eq("user-1"), eq("Usuario"), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(new ArrayList<>());

                when(locationServiceClient.getAllNearby(anyDouble(), anyDouble(), anyDouble()))
                                .thenReturn(List.of());

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertEquals("Receta encontrada", response.getRespuesta());
                assertEquals("conv-3", response.getConversacionId());
                assertNotNull(response.getPrecios());
                assertNotNull(response.getUbicacion());
        }

        @Test
        void process_shouldHandleWrappedRecipeNode() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "harina");

                Map<String, Object> recetaInterna = new HashMap<>();
                recetaInterna.put("ingredientes", List.of(ingrediente));

                Map<String, Object> datos = new HashMap<>();
                datos.put("recipe", recetaInterna);

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-recipe");
                aiResponse.put("datos", datos);

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(new ArrayList<>());

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
        }

        @Test
        void process_shouldHandleWrappedRecetaNode() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "leche");

                Map<String, Object> recetaInterna = new HashMap<>();
                recetaInterna.put("ingredientes", List.of(ingrediente));

                Map<String, Object> datos = new HashMap<>();
                datos.put("receta", recetaInterna);

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-receta");
                aiResponse.put("datos", datos);

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(new ArrayList<>());

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
        }

        @Test
        void processRecipe_shouldTryOriginalIngredientWhenSimplifiedReturnsEmpty() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "jugo de pera");

                Map<String, Object> receta = new HashMap<>();
                receta.put("ingredientes", List.of(ingrediente));

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-fallback");
                aiResponse.put("datos", receta);

                PriceData price = new PriceData();

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices("jugo"))
                                .thenReturn(new ArrayList<>());

                when(freshmartServiceClient.getPrices("jugo de pera"))
                                .thenReturn(List.of(price));

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertEquals(1, response.getPrecios().size());

                verify(freshmartServiceClient).getPrices("jugo");
                verify(freshmartServiceClient).getPrices("jugo de pera");
        }

        @Test
        void processRecipe_shouldHandleIngredientPriceException() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "leche");

                Map<String, Object> receta = new HashMap<>();
                receta.put("ingredientes", List.of(ingrediente));

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-price-error");
                aiResponse.put("datos", receta);

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenThrow(new RuntimeException("sin precio"));

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertNotNull(response.getPrecios());
                assertEquals(0, response.getPrecios().size());
        }

        @Test
        void process_shouldReturnRecipeWithoutLocation() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> receta = new HashMap<>();
                receta.put("receta", "Pan");

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "Ok");
                aiResponse.put("conversacionId", "conv-4");
                aiResponse.put("datos", receta);

                when(aiServiceClient.chat(eq("user-1"), eq("Usuario"), anyMap()))
                                .thenReturn(aiResponse);

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());

                verify(locationServiceClient, never())
                                .getAllNearby(anyDouble(), anyDouble(), anyDouble());
        }

        @Test
        void process_shouldHandleLocationException() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");
                request.setUserLat(-33.0);
                request.setUserLng(-71.0);

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-location-error");
                aiResponse.put("datos", Map.of());

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(locationServiceClient.getAllNearby(anyDouble(), anyDouble(), anyDouble()))
                                .thenThrow(new RuntimeException("Error"));

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertNull(response.getUbicacion());
        }

        @Test
        void process_shouldHandleRecipeException() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "Respuesta");
                aiResponse.put("conversacionId", "conv-5");
                aiResponse.put("datos", "dato_invalido");

                when(aiServiceClient.chat(eq("user-1"), eq("Usuario"), anyMap()))
                                .thenReturn(aiResponse);

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertEquals("Respuesta", response.getRespuesta());
                assertNotNull(response.getPrecios());
        }

        @Test
        void simplificarIngrediente_shouldCoverBasicCases() throws Exception {
                var method = IntentService.class
                                .getDeclaredMethod("simplificarIngrediente", String.class);

                method.setAccessible(true);

                assertEquals("", method.invoke(intentService, (String) null));
                assertEquals("leche", method.invoke(intentService, "Leche Entera"));
                assertEquals("galletas", method.invoke(intentService, "Galletas Oreo"));
                assertEquals("producto raro", method.invoke(intentService, "Producto Raro"));
        }

        @Test
        void simplificarIngrediente_shouldCoverAllMappings() throws Exception {
                var method = IntentService.class
                                .getDeclaredMethod("simplificarIngrediente", String.class);

                method.setAccessible(true);

                assertEquals("mantequilla", method.invoke(intentService, "mantequilla sin sal"));
                assertEquals("azucar", method.invoke(intentService, "azúcar flor"));
                assertEquals("limon", method.invoke(intentService, "limón amarillo"));
                assertEquals("huevo", method.invoke(intentService, "huevos grandes"));
                assertEquals("harina", method.invoke(intentService, "harina con polvos"));
                assertEquals("crema", method.invoke(intentService, "crema batida"));
                assertEquals("aceite", method.invoke(intentService, "aceite vegetal"));
                assertEquals("sal", method.invoke(intentService, "sal fina"));
                assertEquals("vainilla", method.invoke(intentService, "vainilla líquida"));
        }

        @Test
        void simplificarIngrediente_shouldCoverDePatternSpecificIngredients() throws Exception {
                var method = IntentService.class
                                .getDeclaredMethod("simplificarIngrediente", String.class);

                method.setAccessible(true);

                assertEquals("naranja", method.invoke(intentService, "ralladura de naranja"));
                assertEquals("coco", method.invoke(intentService, "ralladura de coco"));
                assertEquals("jugo", method.invoke(intentService, "jugo de manzana"));
        }

        @Test
        void processRecipe_shouldHandleNullPricesFromFreshmart() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "leche");

                Map<String, Object> receta = new HashMap<>();
                receta.put("ingredientes", List.of(ingrediente));

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-null-price");
                aiResponse.put("datos", receta);

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(null);

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertNotNull(response.getPrecios());
                assertEquals(0, response.getPrecios().size());
        }

        @Test
        void processRecipe_shouldAddPricesWhenFreshmartReturnsPrices() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "leche");

                Map<String, Object> receta = new HashMap<>();
                receta.put("ingredientes", List.of(ingrediente));

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-add-price");
                aiResponse.put("datos", receta);

                PriceData price = new PriceData();
                price.setProducto("leche");

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(List.of(price));

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertEquals(1, response.getPrecios().size());
        }

        @Test
        void processSearch_shouldHandleNullProduct() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("buscar algo");

                Map<String, Object> datos = new HashMap<>();

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "search");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-null-product");
                aiResponse.put("datos", datos);

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("search", response.getIntentType());
                assertNotNull(response.getPrecios());
                assertEquals(0, response.getPrecios().size());

                verify(freshmartServiceClient, never()).getPrices(anyString());
        }

        @Test
        void simplificarIngrediente_shouldCoverSpecificDePattern() throws Exception {
                var method = IntentService.class
                                .getDeclaredMethod("simplificarIngrediente", String.class);

                method.setAccessible(true);

                assertEquals("naranja", method.invoke(intentService, "ralladura de naranja"));
                assertEquals("coco", method.invoke(intentService, "ralladura de coco"));
        }

        @Test
        void processRecipe_shouldEnterRecipeWrapperBranch() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "azucar");
                ingrediente.put("cantidad", "1");
                ingrediente.put("unidad", "kg");

                Map<String, Object> recipeNode = new HashMap<>();
                recipeNode.put("receta", "Torta");
                recipeNode.put("ingredientes", List.of(ingrediente));

                Map<String, Object> datos = new HashMap<>();
                datos.put("recipe", recipeNode);

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-wrapper-recipe");
                aiResponse.put("datos", datos);

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(new ArrayList<>());

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertNotNull(response.getReceta());
        }

        @Test
        void processRecipe_shouldEnterRecetaWrapperBranch() {
                IntentRequest request = new IntentRequest();
                request.setMensaje("receta");

                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nombre", "harina");
                ingrediente.put("cantidad", "1");
                ingrediente.put("unidad", "kg");

                Map<String, Object> recetaNode = new HashMap<>();
                recetaNode.put("receta", "Queque");
                recetaNode.put("ingredientes", List.of(ingrediente));

                Map<String, Object> datos = new HashMap<>();
                datos.put("receta", recetaNode);

                Map<String, Object> aiResponse = new HashMap<>();
                aiResponse.put("intentType", "recipe");
                aiResponse.put("respuesta", "ok");
                aiResponse.put("conversacionId", "conv-wrapper-receta");
                aiResponse.put("datos", datos);

                when(aiServiceClient.chat(any(), any(), anyMap()))
                                .thenReturn(aiResponse);

                when(freshmartServiceClient.getPrices(anyString()))
                                .thenReturn(new ArrayList<>());

                IntentResponse response = intentService.process(request, "user-1");

                assertEquals("recipe", response.getIntentType());
                assertNotNull(response.getReceta());
        }
}