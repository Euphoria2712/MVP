package intent_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Solicitud para detectar la intención del usuario")
public class IntentRequest {

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(description = "Mensaje del usuario", example = "¿Dónde puedo encontrar un restaurante cerca de mí?")
    private String mensaje;

    @Schema(description = "ID del usuario")
    private String userId;

    @Schema(description = "Latitud del usuario")
    private Double userLat;
    @Schema(description = "Longitud del usuario")
    private Double userLng;

    @Schema(description = "Radio de búsqueda en kilómetros")
    @DecimalMin(value = "0.1", message = "El radio debe ser mayor a 0")
    private Double radiusKm;
}