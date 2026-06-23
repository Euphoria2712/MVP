package recipe_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import recipe_service.domain.Ingredient;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "Información completa de una receta")
public class RecipeResponse {

    @Schema(example = "6858bb33d34f5f60b2b154e2")
    private String id;

    @Schema(example = "Pan Amasado")
    private String nombre;

    @Schema(example = "Panadería")
    private String categoria;

    @Schema(example = "Media")
    private String dificultad;

    @Schema(example = "90")
    private Integer tiempoMinutos;

    @Schema(example = "8")
    private Integer porciones;

    @Schema(example = "https://misimagenes.com/pan.jpg")
    private String imagenUrl;

    private List<Ingredient> ingredientes;

    @Schema(
            example = "[\"Mezclar ingredientes\", \"Amasar\", \"Hornear\"]"
    )
    private List<String> pasos;

    @Schema(
            example = "[\"casero\", \"horno\", \"tradicional\"]"
    )
    private List<String> tags;

    @Schema(example = "2026-06-23T14:30:00")
    private LocalDateTime createdAt;

    @Schema(example = "2026-06-23T14:45:00")
    private LocalDateTime updatedAt;
}