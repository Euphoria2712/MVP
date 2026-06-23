package recipe_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Información necesaria para registrar una receta")
public class RecipeRequest {

    @Schema(example = "Pan Amasado")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(example = "Panadería")
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @Schema(example = "Media")
    @NotBlank(message = "La dificultad es obligatoria")
    private String dificultad;

    @Schema(example = "90")
    @Min(value = 1, message = "El tiempo debe ser mayor a 0")
    private Integer tiempoMinutos;

    @Schema(example = "8")
    @Min(value = 1, message = "Las porciones deben ser mayor a 0")
    private Integer porciones;

    @Schema(
            example = "https://misimagenes.com/pan.jpg",
            nullable = true
    )
    private String imagenUrl;

    @Valid
    @NotEmpty(message = "Debe tener al menos un ingrediente")
    private List<IngredientRequest> ingredientes;

    @Schema(
            example = "[\"Mezclar ingredientes\", \"Amasar\", \"Hornear\"]"
    )
    @NotEmpty(message = "Debe tener al menos un paso")
    private List<String> pasos;

    @Schema(
            example = "[\"casero\", \"horno\", \"tradicional\"]"
    )
    private List<String> tags;
}