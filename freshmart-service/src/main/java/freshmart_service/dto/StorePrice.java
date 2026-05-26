package freshmart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorePrice {
    private String storeId;        // "simermart"
    private String storeName;      // "SimerMart"
    private Integer precio;        // precio con variación aplicada
    private String unidad;
    private String marca;
    private Boolean disponible;
    private String logoUrl;
}