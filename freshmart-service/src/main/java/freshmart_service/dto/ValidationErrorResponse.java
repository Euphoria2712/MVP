package freshmart_service.dto;

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
@Schema(description = "Respuesta de error de validación")
public class ValidationErrorResponse {

    @Schema(example = "2026-06-22T15:30:00")
    private String timestamp;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Error de validación")
    private String error;

    @Schema(
            description = "Lista de errores por campo",
            example = "{\"nombre\":\"El nombre del producto es obligatorio\",\"precioBase\":\"El precio base debe ser mayor a 0\"}"
    )
    private Map<String, String> messages;
}