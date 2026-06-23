package location_service.dto;

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
@Schema(description = "Resultado de la búsqueda de sucursales cercanas")
public class NearbyStoresResponse {

    @Schema(example = "SimerMart")
    private String storeName;

    @Schema(example = "3")
    private int totalEncontradas;

    @Schema(description = "Lista de sucursales encontradas")
    private List<StoreLocation> sucursales;
}