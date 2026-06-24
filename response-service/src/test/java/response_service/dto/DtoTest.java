package response_service.dto;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void chatRequest_shouldWork() {
        ChatRequest dto = new ChatRequest();

        dto.setMensaje("Hola");
        dto.setUserLat(-33.0);
        dto.setUserLng(-71.0);
        dto.setRadiusKm(5.0);

        assertEquals("Hola", dto.getMensaje());
        assertEquals(-33.0, dto.getUserLat());
        assertEquals(-71.0, dto.getUserLng());
        assertEquals(5.0, dto.getRadiusKm());
    }

    @Test
    void errorResponse_shouldWork() {
        ErrorResponse dto = ErrorResponse.builder()
                .timestamp("2026")
                .status(400)
                .error("Error")
                .message("Mensaje")
                .build();

        assertEquals("2026", dto.getTimestamp());
        assertEquals(400, dto.getStatus());
        assertEquals("Error", dto.getError());
        assertEquals("Mensaje", dto.getMessage());
    }

    @Test
    void validationErrorResponse_shouldWork() {
        ValidationErrorResponse dto =
                ValidationErrorResponse.builder()
                        .timestamp("2026")
                        .status(400)
                        .error("Validacion")
                        .messages(Map.of("mensaje", "obligatorio"))
                        .build();

        assertEquals(400, dto.getStatus());
        assertEquals(
                "obligatorio",
                dto.getMessages().get("mensaje")
        );
    }

    @Test
    void recipeCard_shouldWork() {

        RecipeCard.Ingredient ingredient =
                new RecipeCard.Ingredient();

        ingredient.setNombre("Leche");
        ingredient.setCantidad("1");
        ingredient.setUnidad("Litro");

        RecipeCard recipe =
                RecipeCard.builder()
                        .nombre("Panqueques")
                        .dificultad("Fácil")
                        .tiempoMinutos(30)
                        .porciones(4)
                        .pasos(List.of("Paso 1"))
                        .ingredientes(List.of(ingredient))
                        .build();

        assertEquals("Panqueques", recipe.getNombre());
        assertEquals("Fácil", recipe.getDificultad());
        assertEquals(30, recipe.getTiempoMinutos());
        assertEquals(4, recipe.getPorciones());

        assertEquals(
                "Leche",
                recipe.getIngredientes().get(0).getNombre()
        );
    }

    @Test
    void recipeWrapper_shouldWork() {

        RecipeCard recipe =
                RecipeCard.builder()
                        .nombre("Arroz")
                        .build();

        RecipeWrapper wrapper = new RecipeWrapper();
        wrapper.setReceta(recipe);

        assertEquals(
                "Arroz",
                wrapper.getReceta().getNombre()
        );
    }

    @Test
    void priceCard_shouldWork() {

        PriceCard.StorePrice store =
                new PriceCard.StorePrice();

        store.setStoreId("1");
        store.setStoreName("Lider");
        store.setPrecio(1000);
        store.setDisponible(true);

        PriceCard card =
                PriceCard.builder()
                        .producto("Arroz")
                        .unidad("kg")
                        .masBarato(store)
                        .comparacion(List.of(store))
                        .masBaratoPrecio(1000)
                        .masBaratoTienda("Lider")
                        .build();

        assertEquals("Arroz", card.getProducto());
        assertEquals("kg", card.getUnidad());
        assertEquals("Lider", card.getMasBaratoTienda());
        assertEquals(1000, card.getMasBaratoPrecio());

        assertEquals(
                "Lider",
                card.getMasBarato().getStoreName()
        );
    }

    @Test
    void finalResponse_shouldWork() {

        FinalResponse response =
                FinalResponse.builder()
                        .tipo("recipe")
                        .mensaje("ok")
                        .conversacionId("conv-1")
                        .costoEstimado(1000)
                        .tiendaRecomendada("Lider")
                        .build();

        assertEquals("recipe", response.getTipo());
        assertEquals("ok", response.getMensaje());
        assertEquals("conv-1", response.getConversacionId());
        assertEquals(1000, response.getCostoEstimado());
        assertEquals("Lider", response.getTiendaRecomendada());
    }
}