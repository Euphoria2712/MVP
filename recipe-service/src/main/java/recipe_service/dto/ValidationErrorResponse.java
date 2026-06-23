package recipe_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta para errores de validación")
public class ValidationErrorResponse {

    @Schema(
            description = "Fecha y hora del error",
            example = "2026-06-23T15:30:00"
    )
    private String timestamp;

    @Schema(
            description = "Código HTTP",
            example = "400"
    )
    private int status;

    @Schema(
            description = "Tipo de error",
            example = "Error de validación"
    )
    private String error;

    @Schema(
            description = "Errores por campo",
            example = "{\"nombre\":\"El nombre es obligatorio\",\"porciones\":\"Las porciones deben ser mayor a 0\"}"
    )
    private Map<String, String> messages;
}