package recipe_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar para errores")
public class ErrorResponse {

    @Schema(
            description = "Fecha y hora del error",
            example = "2026-06-23T15:30:00"
    )
    private String timestamp;

    @Schema(
            description = "Código HTTP",
            example = "404"
    )
    private int status;

    @Schema(
            description = "Tipo de error",
            example = "Error de negocio"
    )
    private String error;

    @Schema(
            description = "Mensaje detallado del error",
            example = "La receta no fue encontrada"
    )
    private String message;
}