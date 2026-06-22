package intent_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IntentRequest {

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    private String userId;

    private Double userLat;
    private Double userLng;

    @DecimalMin(value = "0.1", message = "El radio debe ser mayor a 0")
    private Double radiusKm;
}