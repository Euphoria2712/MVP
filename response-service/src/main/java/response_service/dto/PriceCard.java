package response_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCard {
    private String producto;
    private String unidad;
    private String masBaratoTienda;
    private Integer masBaratoPrecio;
    private List<StorePrice> comparacion;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StorePrice {
        private String tienda;
        private Integer precio;
        private Boolean disponible;
    }
}