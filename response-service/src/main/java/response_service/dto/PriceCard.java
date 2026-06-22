package response_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCard {
    private String producto;
    private String unidad;
    
    @JsonProperty("masBarato") // Coincide con PriceData del intent
    private StorePrice masBarato; 

    @JsonProperty("precios") // Aquí estaba el error, el intent manda "precios"
    private List<StorePrice> comparacion;

    // Estos campos los calcularemos en el Service o los dejamos si el intent los manda
    private String masBaratoTienda;
    private Integer masBaratoPrecio;

    @Data
    public static class StorePrice {
        private String storeId;
        private String storeName; // En el intent es storeName, asegúrate que coincida
        private Integer precio;
        private Boolean disponible;
    }
}