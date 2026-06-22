package intent_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeData {
    private String receta;
    private List<Ingredient> ingredientes;
    private List<String> pasos;
    private Integer tiempoMinutos;
    private String dificultad;

    @Data
    public static class Ingredient {
        private String nombre;
        private String cantidad;
        private String unidad;
    }
}