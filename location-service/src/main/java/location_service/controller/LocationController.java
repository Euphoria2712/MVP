package location_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import location_service.dto.ErrorResponse;
import location_service.dto.LocationRequest;
import location_service.dto.NearbyStoresResponse;
import location_service.dto.ValidationErrorResponse;
import location_service.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@Tag(
        name = "Location Service",
        description = "API para búsqueda de sucursales cercanas según la ubicación del usuario"
)
public class LocationController {

    private final LocationService locationService;

    @Operation(
            summary = "Buscar sucursales cercanas",
            description = "Obtiene las sucursales de una cadena de supermercados dentro de un radio determinado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucursales encontradas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NearbyStoresResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/search")
    public NearbyStoresResponse searchStores(@RequestBody LocationRequest request) {
        return locationService.searchStores(request);
    }
}