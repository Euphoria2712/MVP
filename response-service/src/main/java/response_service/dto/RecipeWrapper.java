package response_service.dto;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "Wrapper para la receta")
public class RecipeWrapper {
    @Schema (description = "Receta")
    private RecipeCard receta; 
}