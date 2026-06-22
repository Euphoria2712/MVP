package recipe_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RecipeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotBlank(message = "La dificultad es obligatoria")
    private String dificultad;

    @Min(value = 1, message = "El tiempo debe ser mayor a 0")
    private Integer tiempoMinutos;

    @Min(value = 1, message = "Las porciones deben ser mayor a 0")
    private Integer porciones;

    private String imagenUrl;

    @Valid
    @NotEmpty(message = "Debe tener al menos un ingrediente")
    private List<IngredientRequest> ingredientes;

    @NotEmpty(message = "Debe tener al menos un paso")
    private List<String> pasos;

    private List<String> tags;
}