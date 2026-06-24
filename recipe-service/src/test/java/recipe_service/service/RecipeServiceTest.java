package recipe_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import recipe_service.domain.Ingredient;
import recipe_service.domain.Recipe;
import recipe_service.dto.IngredientRequest;
import recipe_service.dto.RecipeRequest;
import recipe_service.dto.RecipeResponse;
import recipe_service.exception.ResourceNotFoundException;
import recipe_service.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe recipe() {
        Ingredient ingredient = Ingredient.builder()
                .nombre("Harina")
                .cantidad("1")
                .unidad("taza")
                .build();

        return Recipe.builder()
                .id("recipe-1")
                .nombre("Panqueques")
                .categoria("Postre")
                .dificultad("Fácil")
                .tiempoMinutos(30)
                .porciones(4)
                .imagenUrl("img.png")
                .ingredientes(List.of(ingredient))
                .pasos(List.of("Mezclar", "Cocinar"))
                .tags(List.of("dulce", "rápido"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
        request.setTags(List.of("dulce", "rápido"));

        return request;
    }

    @Test
    void getAll_shouldReturnRecipes() {
        when(recipeRepository.findAll())
                .thenReturn(List.of(recipe()));

        List<RecipeResponse> result = recipeService.getAll();

        assertEquals(1, result.size());
        assertEquals("Panqueques", result.get(0).getNombre());
    }

    @Test
    void getById_shouldReturnRecipe() {
        when(recipeRepository.findById("recipe-1"))
                .thenReturn(Optional.of(recipe()));

        RecipeResponse result = recipeService.getById("recipe-1");

        assertEquals("recipe-1", result.getId());
        assertEquals("Panqueques", result.getNombre());
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(recipeRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recipeService.getById("bad-id"));
    }

    @Test
    void search_shouldReturnRecipes() {
        when(recipeRepository.findByNombreContainingIgnoreCase("pan"))
                .thenReturn(List.of(recipe()));

        List<RecipeResponse> result = recipeService.search("pan");

        assertEquals(1, result.size());
        assertEquals("Panqueques", result.get(0).getNombre());
    }

    @Test
    void getByCategory_shouldReturnRecipes() {
        when(recipeRepository.findByCategoria("Postre"))
                .thenReturn(List.of(recipe()));

        List<RecipeResponse> result = recipeService.getByCategory("Postre");

        assertEquals(1, result.size());
        assertEquals("Postre", result.get(0).getCategoria());
    }

    @Test
    void getByTag_shouldReturnRecipes() {
        when(recipeRepository.findByTagsContaining("dulce"))
                .thenReturn(List.of(recipe()));

        List<RecipeResponse> result = recipeService.getByTag("dulce");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getTags().contains("dulce"));
    }

    @Test
    void create_shouldCreateRecipe() {
        RecipeRequest request = request();

        when(recipeRepository.save(any(Recipe.class)))
                .thenAnswer(invocation -> {
                    Recipe recipe = invocation.getArgument(0);
                    recipe.setId("recipe-1");
                    return recipe;
                });

        RecipeResponse result = recipeService.create(request);

        assertEquals("recipe-1", result.getId());
        assertEquals("Panqueques", result.getNombre());
        assertEquals(1, result.getIngredientes().size());

        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void update_shouldUpdateRecipe() {
        Recipe existing = recipe();
        RecipeRequest request = request();
        request.setNombre("Panqueques actualizados");

        when(recipeRepository.findById("recipe-1"))
                .thenReturn(Optional.of(existing));

        when(recipeRepository.save(any(Recipe.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RecipeResponse result = recipeService.update("recipe-1", request);

        assertEquals("Panqueques actualizados", result.getNombre());
        assertEquals("Postre", result.getCategoria());

        verify(recipeRepository).save(existing);
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(recipeRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recipeService.update("bad-id", request()));

        verify(recipeRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteRecipe() {
        Recipe recipe = recipe();

        when(recipeRepository.findById("recipe-1"))
                .thenReturn(Optional.of(recipe));

        recipeService.delete("recipe-1");

        verify(recipeRepository).delete(recipe);
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(recipeRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recipeService.delete("bad-id"));

        verify(recipeRepository, never()).delete(any());
    }
}