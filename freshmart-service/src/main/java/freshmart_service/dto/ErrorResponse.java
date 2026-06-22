package freshmart_service.dto;

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

    @Schema(example = "404")
    private int status;

    @Schema(example = "Recurso no encontrado")
    private String error;

    @Schema(example = "Producto con id 1 no existe")
    private String message;
}