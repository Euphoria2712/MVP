package recipe_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recipe_service.dto.ErrorResponse;
import recipe_service.dto.RecipeRequest;
import recipe_service.dto.RecipeResponse;
import recipe_service.dto.ValidationErrorResponse;
import recipe_service.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(
        name = "Recipe Service",
        description = "API para la gestión de recetas"
)
public class RecipeController {

    private final RecipeService recipeService;

    @Operation(
            summary = "Obtener todas las recetas",
            description = "Devuelve la lista completa de recetas registradas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de recetas obtenida correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RecipeResponse.class)
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAll() {
        return ResponseEntity.ok(recipeService.getAll());
    }

    @Operation(
            summary = "Buscar receta por ID",
            description = "Obtiene una receta específica mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Receta encontrada",
                    content = @Content(
                            schema = @Schema(implementation = RecipeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Receta no encontrada",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @Operation(
            summary = "Buscar recetas por nombre",
            description = "Busca recetas cuyo nombre coincida con el texto ingresado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda realizada correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RecipeResponse.class)
                            )
                    )
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponse>> search(
            @RequestParam String q) {

        return ResponseEntity.ok(recipeService.search(q));
    }

    @Operation(
            summary = "Buscar recetas por categoría",
            description = "Obtiene todas las recetas pertenecientes a una categoría"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Recetas encontradas",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RecipeResponse.class)
                            )
                    )
            )
    })
    @GetMapping("/category/{categoria}")
    public ResponseEntity<List<RecipeResponse>> getByCategory(
            @PathVariable String categoria) {

        return ResponseEntity.ok(recipeService.getByCategory(categoria));
    }

    @Operation(
            summary = "Buscar recetas por etiqueta",
            description = "Obtiene las recetas asociadas a una etiqueta"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Recetas encontradas",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RecipeResponse.class)
                            )
                    )
            )
    })
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<RecipeResponse>> getByTag(
            @PathVariable String tag) {

        return ResponseEntity.ok(recipeService.getByTag(tag));
    }

    @Operation(
            summary = "Crear una receta",
            description = "Registra una nueva receta en el sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Receta creada exitosamente",
                    content = @Content(
                            schema = @Schema(implementation = RecipeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación",
                    content = @Content(
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<RecipeResponse> create(
            @Valid @RequestBody RecipeRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recipeService.create(request));
    }

    @Operation(
            summary = "Actualizar una receta",
            description = "Actualiza la información de una receta existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Receta actualizada correctamente",
                    content = @Content(
                            schema = @Schema(implementation = RecipeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación",
                    content = @Content(
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Receta no encontrada",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> update(
            @PathVariable String id,
            @Valid @RequestBody RecipeRequest request) {

        return ResponseEntity.ok(recipeService.update(id, request));
    }

    @Operation(
            summary = "Eliminar una receta",
            description = "Elimina una receta mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Receta eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Receta no encontrada",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}