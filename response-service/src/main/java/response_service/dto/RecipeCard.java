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
public class RecipeCard {
    private String nombre;
    private String dificultad;
    private Integer tiempoMinutos;
    private Integer porciones;
    private List<String> pasos;
    private List<Ingredient> ingredientes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ingredient {
        private String nombre;
        private String cantidad;
        private String unidad;
    }
}