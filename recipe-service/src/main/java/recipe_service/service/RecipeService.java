package recipe_service.service;

import lombok.RequiredArgsConstructor;
import recipe_service.domain.Recipe;
import recipe_service.dto.IngredientRequest;
import recipe_service.dto.RecipeResponse;
import recipe_service.repository.RecipeRepository;
import recipe_service.domain.Ingredient;
import recipe_service.dto.RecipeRequest;
import recipe_service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final Logger log = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;

    public List<RecipeResponse> getAll() {

        log.info("Listando todas las recetas");

        return recipeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RecipeResponse getById(String id) {

        log.info("Buscando receta id={}", id);

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Receta no encontrada id={}", id);
                    return new ResourceNotFoundException("Receta no encontrada");
                });

        return toResponse(recipe);
    }

    public List<RecipeResponse> search(String query) {

        log.info("Buscando recetas por nombre query={}", query);

        return recipeRepository.findByNombreContainingIgnoreCase(query)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RecipeResponse> getByCategory(String categoria) {

        log.info("Buscando recetas categoria={}", categoria);

        return recipeRepository.findByCategoria(categoria)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RecipeResponse> getByTag(String tag) {

        log.info("Buscando recetas tag={}", tag);

        return recipeRepository.findByTagsContaining(tag)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RecipeResponse create(RecipeRequest request) {

        log.info("Creando receta nombre={}", request.getNombre());

        Recipe recipe = Recipe.builder()
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .dificultad(request.getDificultad())
                .tiempoMinutos(request.getTiempoMinutos())
                .porciones(request.getPorciones())
                .imagenUrl(request.getImagenUrl())
                .ingredientes(toIngredients(request.getIngredientes()))
                .pasos(request.getPasos())
                .tags(request.getTags())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        log.info("Receta creada correctamente id={}", savedRecipe.getId());

        return toResponse(savedRecipe);
    }

    public RecipeResponse update(String id, RecipeRequest request) {

        log.info("Actualizando receta id={}", id);

        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Receta no encontrada id={}", id);
                    return new ResourceNotFoundException("Receta no encontrada");
                });

        existing.setNombre(request.getNombre());
        existing.setCategoria(request.getCategoria());
        existing.setDificultad(request.getDificultad());
        existing.setTiempoMinutos(request.getTiempoMinutos());
        existing.setPorciones(request.getPorciones());
        existing.setImagenUrl(request.getImagenUrl());
        existing.setIngredientes(toIngredients(request.getIngredientes()));
        existing.setPasos(request.getPasos());
        existing.setTags(request.getTags());
        existing.setUpdatedAt(LocalDateTime.now());

        Recipe updatedRecipe = recipeRepository.save(existing);

        log.info("Receta actualizada correctamente id={}", id);

        return toResponse(updatedRecipe);
    }

    public void delete(String id) {

        log.info("Eliminando receta id={}", id);

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Receta no encontrada id={}", id);
                    return new ResourceNotFoundException("Receta no encontrada");
                });

        recipeRepository.delete(recipe);

        log.info("Receta eliminada correctamente id={}", id);
    }

    private RecipeResponse toResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .nombre(recipe.getNombre())
                .categoria(recipe.getCategoria())
                .dificultad(recipe.getDificultad())
                .tiempoMinutos(recipe.getTiempoMinutos())
                .porciones(recipe.getPorciones())
                .imagenUrl(recipe.getImagenUrl())
                .ingredientes(recipe.getIngredientes())
                .pasos(recipe.getPasos())
                .tags(recipe.getTags())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .build();
    }

    private List<Ingredient> toIngredients(List<IngredientRequest> requests) {
        return requests.stream()
                .map(i -> Ingredient.builder()
                        .nombre(i.getNombre())
                        .cantidad(i.getCantidad())
                        .unidad(i.getUnidad())
                        .build())
                .toList();
    }

}