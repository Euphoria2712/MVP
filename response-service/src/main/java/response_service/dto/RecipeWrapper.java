package response_service.dto;

import lombok.Data;

@Data
public class RecipeWrapper {
    // Esta propiedad se llama igual que la llave del JSON
    private RecipeCard receta; 
}