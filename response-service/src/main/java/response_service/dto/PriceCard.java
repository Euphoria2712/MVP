package response_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tarjeta de precios de un producto")
public class PriceCard {

    @Schema(example = "Leche")
    private String producto;
    @Schema(example = "litro")
    private String unidad;

    @JsonProperty("masBarato")
    @Schema(description = "Información del precio más barato")
    private StorePrice masBarato;

    @JsonProperty("precios")
    @Schema(description = "Lista de precios en diferentes tiendas")
    private List<StorePrice> comparacion;
    @Schema(description = "Nombre de la tienda con el precio más barato")
    private String masBaratoTienda;
    @Schema(example = "15")
    private Integer masBaratoPrecio;

    @Data
    public static class StorePrice {
        @Schema(example = "store_123")
        private String storeId;
        @Schema(example = "Supermercado XYZ")
        private String storeName;
        @Schema(example = "15")
        private Integer precio;
        @Schema(example = "true")
        private Boolean disponible;
    }
}