package recipe_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import recipe_service.dto.IngredientRequest;
import recipe_service.dto.RecipeRequest;
import recipe_service.dto.RecipeResponse;
import recipe_service.service.RecipeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private RecipeResponse response() {
        return RecipeResponse.builder()
                .id("recipe-1")
                .nombre("Panqueques")
                .categoria("Postre")
                .dificultad("Fácil")
                .tiempoMinutos(30)
                .porciones(4)
                .imagenUrl("img.png")
                .pasos(List.of("Mezclar", "Cocinar"))
                .tags(List.of("dulce"))
                .build();
    }

    private RecipeRequest request() {
        IngredientRequest ingredient = new IngredientRequest();
        ingredient.setNombre("Harina");
        ingredient.setCantidad("1");
        ingredient.setUnidad("taza");

        RecipeRequest request = new RecipeRequest();
        request.setNombre("Panqueques");
        request.setCategoria("Postre");
        request.setDificultad("Fácil");
        request.setTiempoMinutos(30);
        request.setPorciones(4);
        request.setImagenUrl("img.png");
        request.setIngredientes(List.of(ingredient));
        request.setPasos(List.of("Mezclar", "Cocinar"));
        request.setTags(List.of("dulce"));

        return request;
    }

    @Test
    void getAll_shouldReturnRecipes() {
        when(recipeService.getAll())
                .thenReturn(List.of(response()));

        ResponseEntity<List<RecipeResponse>> result = recipeController.getAll();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());

        verify(recipeService).getAll();
    }

    @Test
    void getById_shouldReturnRecipe() {
        when(recipeService.getById("recipe-1"))
                .thenReturn(response());

        ResponseEntity<RecipeResponse> result = recipeController.getById("recipe-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals("recipe-1", result.getBody().getId());

        verify(recipeService).getById("recipe-1");
    }

    @Test
    void search_shouldReturnRecipes() {
        when(recipeService.search("pan"))
                .thenReturn(List.of(response()));

        ResponseEntity<List<RecipeResponse>> result = recipeController.search("pan");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());

        verify(recipeService).search("pan");
    }

    @Test
    void getByCategory_shouldReturnRecipes() {
        when(recipeService.getByCategory("Postre"))
                .thenReturn(List.of(response()));

        ResponseEntity<List<RecipeResponse>> result = recipeController.getByCategory("Postre");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());

        verify(recipeService).getByCategory("Postre");
    }

    @Test
    void getByTag_shouldReturnRecipes() {
        when(recipeService.getByTag("dulce"))
                .thenReturn(List.of(response()));

        ResponseEntity<List<RecipeResponse>> result = recipeController.getByTag("dulce");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());

        verify(recipeService).getByTag("dulce");
    }

    @Test
    void create_shouldReturnCreatedRecipe() {
        RecipeRequest request = request();

        when(recipeService.create(request))
                .thenReturn(response());

        ResponseEntity<RecipeResponse> result = recipeController.create(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("Panqueques", result.getBody().getNombre());

        verify(recipeService).create(request);
    }

    @Test
    void update_shouldReturnUpdatedRecipe() {
        RecipeRequest request = request();

        when(recipeService.update("recipe-1", request))
                .thenReturn(response());

        ResponseEntity<RecipeResponse> result = recipeController.update("recipe-1", request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("recipe-1", result.getBody().getId());

        verify(recipeService).update("recipe-1", request);
    }

    @Test
    void delete_shouldReturnNoContent() {
        ResponseEntity<Void> result = recipeController.delete("recipe-1");

        assertEquals(204, result.getStatusCode().value());

        verify(recipeService).delete("recipe-1");
    }
}