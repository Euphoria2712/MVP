package recipe_service.dto;

import lombok.Builder;
import lombok.Data;
import recipe_service.domain.Ingredient;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RecipeResponse {

    private String id;
    private String nombre;
    private String categoria;
    private String dificultad;
    private Integer tiempoMinutos;
    private Integer porciones;
    private String imagenUrl;

    private List<Ingredient> ingredientes;
    private List<String> pasos;
    private List<String> tags;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}