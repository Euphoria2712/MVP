package response_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar de error")
public class ErrorResponse {

    @Schema(example = "2026-06-22T15:30:00")
    private String timestamp;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Error de negocio")
    private String error;

    @Schema(example = "No se pudo procesar la solicitud")
    private String message;
}