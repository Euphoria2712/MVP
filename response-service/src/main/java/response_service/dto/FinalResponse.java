package response_service.dto;

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
@Schema(description = "Respuesta final del servicio")
public class FinalResponse {

    @Schema(example = "recipe")
    private String tipo;
    @Schema(example = "Aquí está la receta que pediste")
    private String mensaje;
    @Schema(example = "conv_123456")
    private String conversacionId;
    @Schema(description = "Tarjeta de receta")
    private RecipeCard receta;
    @Schema(description = "Lista de precios de ingredientes")
    private List<PriceCard> precios;
    @Schema(description = "Lista de sucursales cercanas")
    private Object sucursales;
    @Schema(example = "15")
    private Integer costoEstimado;
    @Schema(example = "Supermercado XYZ")
    private String tiendaRecomendada;
}