package freshmart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceResult {
    private String producto;
    private String unidad;
    private List<StorePrice> precios;  // ordenados de menor a mayor
    private StorePrice masBarato;      // el más barato destacado
}