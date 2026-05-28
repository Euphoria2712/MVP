package response_service.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String mensaje;
    private Double userLat;
    private Double userLng;
    private Double radiusKm;
}