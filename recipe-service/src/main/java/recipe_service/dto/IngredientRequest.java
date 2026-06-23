package recipe_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Ingrediente utilizado en la receta")
public class IngredientRequest {

    @Schema(
            description = "Nombre del ingrediente",
            example = "Harina"
    )
    @NotBlank(message = "El nombre del ingrediente es obligatorio")
    private String nombre;

    @Schema(
            description = "Cantidad requerida",
            example = "500"
    )
    @NotBlank(message = "La cantidad es obligatoria")
    private String cantidad;

    @Schema(
            description = "Unidad de medida",
            example = "gramos"
    )
    @NotBlank(message = "La unidad es obligatoria")
    private String unidad;
}