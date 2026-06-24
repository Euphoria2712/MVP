package location_service.controller;

import location_service.dto.LocationRequest;
import location_service.dto.NearbyStoresResponse;
import location_service.dto.StoreLocation;
import location_service.service.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @Test
    void searchStores_shouldReturnNearbyStoresResponse() {
        LocationRequest request = new LocationRequest();
        request.setStoreName("simermart");
        request.setLat(-33.5117);
        request.setLng(-70.7677);
        request.setRadiusKm(5.0);

        StoreLocation store = StoreLocation.builder()
                .nombre("SimerMart Maipú")
                .direccion("Av. Pajaritos 3261, Maipú")
                .lat(-33.5117)
                .lng(-70.7677)
                .horario("08:00 - 22:00")
                .distanciaKm(0.0)
                .mapsUrl("https://maps.google.com")
                .build();

        NearbyStoresResponse serviceResponse = NearbyStoresResponse.builder()
                .storeName("simermart")
                .totalEncontradas(1)
                .sucursales(List.of(store))
                .build();

        when(locationService.searchStores(request))
                .thenReturn(serviceResponse);

        NearbyStoresResponse response = locationController.searchStores(request);

        assertEquals("simermart", response.getStoreName());
        assertEquals(1, response.getTotalEncontradas());
        assertEquals("SimerMart Maipú", response.getSucursales().get(0).getNombre());

        verify(locationService).searchStores(request);
    }
}