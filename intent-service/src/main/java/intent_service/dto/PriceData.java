package intent_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class PriceData {
    private String producto;
    private String unidad;
    private List<StorePrice> precios;
    private StorePrice masBarato;

    @Data
    public static class StorePrice {
        private String storeId;
        private String storeName;
        private Integer precio;
        private Boolean disponible;
    }
}