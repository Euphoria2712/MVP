package freshmart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Precio de un producto en una tienda")
public class StorePrice {
    @Schema(description = "Identificador de la tienda")
    private String storeId; 
    @Schema(description = "Nombre de la tienda")
    private String storeName; 
    @Schema(description = "Precio del producto")
    private Integer precio; 
    @Schema(description = "Unidad de medida")
    private String unidad;
    @Schema(description = "Marca del producto")
    private String marca;
    @Schema(description = "Disponibilidad del producto")
    private Boolean disponible;
    @Schema(description = "URL del logo de la tienda")
    private String logoUrl;
}