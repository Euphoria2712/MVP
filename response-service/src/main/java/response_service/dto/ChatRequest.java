package response_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Solicitud de chat")
public class ChatRequest {

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(example = "Hola, ¿cómo estás?")
    private String mensaje;

    @Schema(example = "40.7128")
    private Double userLat;
    @Schema(example = "-74.0060")
    private Double userLng;

    @DecimalMin(value = "0.1", message = "El radio debe ser mayor a 0")
    @Schema(example = "1.0")
    private Double radiusKm;
}