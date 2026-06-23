package location_service.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Datos enviados por el usuario para buscar sucursales cercanas")
public class LocationRequest {

    @Schema(description = "Latitud del usuario", example = "-33.4489")
    private double lat;

    @Schema(description = "Longitud del usuario", example = "-70.6693")
    private double lng;

    @Schema(
            description = "Nombre de la cadena de supermercados",
            example = "SimerMart"
    )
    private String storeName;

    @Schema(description = "Radio de búsqueda en kilómetros", example = "10")
    private double radiusKm;
}