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
public class FinalResponse {
    private String tipo;              // "recipe", "search", "chat"
    private String mensaje;           // texto principal
    private String conversacionId;
    private RecipeCard receta;        // si es receta
    private List<PriceCard> precios;  // precios de ingredientes
    private Object sucursales;        // tiendas cercanas
    private Integer costoEstimado;    // suma del ingrediente más barato
    private String tiendaRecomendada; // tienda con mejor precio total
}