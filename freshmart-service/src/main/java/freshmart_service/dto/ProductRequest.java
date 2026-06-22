package freshmart_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Request para crear o actualizar un producto")
public class ProductRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Schema(description = "Nombre del producto")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    @Schema(description = "Categoría del producto")
    private String categoria;

    @NotBlank(message = "La unidad es obligatoria")
    @Schema(description = "Unidad de medida del producto")
    private String unidad;

    @NotBlank(message = "La marca es obligatoria")
    @Schema(description = "Marca del producto")
    private String marca;

    @NotNull(message = "El precio base es obligatorio")
    @Min(value = 1, message = "El precio base debe ser mayor a 0")
    @Schema(description = "Precio base del producto")
    private Integer precioBase;

    @Schema(description = "URL de la imagen del producto")
    private String imagenUrl;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Schema(description = "Disponibilidad del producto")
    private Boolean disponible;
}