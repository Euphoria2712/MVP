package freshmart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resultado de la búsqueda de precios")
public class PriceResult {
    @Schema(description = "Nombre del producto")
    private String producto;
    @Schema(description = "Unidad de medida")
    private String unidad;
    @Schema(description = "Lista de precios por tienda")
    private List<StorePrice> precios; 
    @Schema(description = "Precio más barato")
    private StorePrice masBarato;     
}