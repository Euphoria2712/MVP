package freshmart_service.dto;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Response de un producto")
public class ProductResponse {

    @Schema(description = "ID del producto")
    private String id;
    @Schema(description = "Nombre del producto")
    private String nombre;
    @Schema(description = "Categoría del producto")
    private String categoria;
    @Schema(description = "Unidad de medida del producto")
    private String unidad;
    @Schema(description = "Marca del producto")
    private String marca;
    @Schema(description = "Precio base del producto")
    private Integer precioBase;
    @Schema(description = "URL de la imagen del producto")
    private String imagenUrl;
    @Schema(description = "Disponibilidad del producto")
    private Boolean disponible;
}