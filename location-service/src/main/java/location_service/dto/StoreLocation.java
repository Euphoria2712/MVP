package location_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de una sucursal encontrada")
public class StoreLocation {

    @Schema(example = "SimerMart Providencia")
    private String nombre;

    @Schema(example = "Av. Providencia 1234")
    private String direccion;

    @Schema(example = "-33.4321")
    private double lat;

    @Schema(example = "-70.6123")
    private double lng;

    @Schema(example = "3.5")
    private double distanciaKm;

    @Schema(example = "09:00 - 21:00")
    private String horario;

    @Schema(example = "https://maps.google.com/?q=-33.4321,-70.6123")
    private String mapsUrl;
}