package intent_service.dto;

import lombok.Data;

@Data
public class IntentRequest {
    private String mensaje;
    private String userId;
    private Double userLat;        // GPS del usuario
    private Double userLng;
    private Double radiusKm;       // radio de búsqueda (default 10)
}