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
public class RecipeCard {
   @JsonProperty("receta") // Mapea 'receta' del intent a 'nombre' aquí
    private String nombre;
    private String dificultad;
    private Integer tiempoMinutos;
    private Integer porciones;
    private List<String> pasos;
    private List<Ingredient> ingredientes;

    @Data
    public static class Ingredient {
        private String nombre;
        private String cantidad;
        private String unidad;
    }
}