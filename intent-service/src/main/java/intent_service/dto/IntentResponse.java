package intent_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntentResponse {
    private String intentType;       // "recipe", "search", "chat"
    private String respuesta;        // texto para mostrar al usuario
    private String conversacionId;
    private RecipeData receta;       // si es receta
    private List<PriceData> precios; // precios de ingredientes
    private Object ubicacion;        // sucursales cercanas
}