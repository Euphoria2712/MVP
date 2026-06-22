package freshmart_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotBlank(message = "La unidad es obligatoria")
    private String unidad;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotNull(message = "El precio base es obligatorio")
    @Min(value = 1, message = "El precio base debe ser mayor a 0")
    private Integer precioBase;

    private String imagenUrl;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;
}