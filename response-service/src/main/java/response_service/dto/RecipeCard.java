package response_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tarjeta de receta")
public class RecipeCard {

    @JsonProperty("receta") 
    private String nombre;
    @Schema(example = "Fácil")
    private String dificultad;
    @Schema(example = "30")
    private Integer tiempoMinutos;
    @Schema(example = "4")
    private Integer porciones;
    @Schema(description = "Lista de pasos para preparar la receta")
    private List<String> pasos;
    @Schema(description = "Lista de ingredientes necesarios")
    private List<Ingredient> ingredientes;

    @Data
    public static class Ingredient {
        @Schema(example = "Leche")
        private String nombre;
        @Schema(example = "1")
        private String cantidad;
        @Schema(example = "litro")
        private String unidad;
    }
}