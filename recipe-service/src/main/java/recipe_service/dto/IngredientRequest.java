package recipe_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IngredientRequest {

    @NotBlank(message = "El nombre del ingrediente es obligatorio")
    private String nombre;

    @NotBlank(message = "La cantidad es obligatoria")
    private String cantidad;

    @NotBlank(message = "La unidad es obligatoria")
    private String unidad;
}