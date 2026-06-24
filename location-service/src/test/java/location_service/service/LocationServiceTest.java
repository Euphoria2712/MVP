package location_service.service;

import location_service.dto.LocationRequest;
import location_service.dto.NearbyStoresResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationService locationService;

    @Test
    void getNearbyStores_shouldReturnStoresInsideRadius() {
        LocationRequest request = new LocationRequest();
        request.setStoreName("simermart");
        request.setLat(-33.5117);
        request.setLng(-70.7677);
        request.setRadiusKm(5.0);

        NearbyStoresResponse response =
                locationService.getNearbyStores(request);

        assertEquals("simermart", response.getStoreName());
        assertTrue(response.getTotalEncontradas() > 0);
        assertFalse(response.getSucursales().isEmpty());
        assertEquals("SimerMart Maipú", response.getSucursales().get(0).getNombre());
        assertNotNull(response.getSucursales().get(0).getMapsUrl());
    }

    @Test
    void getNearbyStores_shouldReturnEmptyWhenStoreDoesNotExist() {
        LocationRequest request = new LocationRequest();
        request.setStoreName("tienda-inexistente");
        request.setLat(-33.5117);
        request.setLng(-70.7677);
        request.setRadiusKm(5.0);

        NearbyStoresResponse response =
                locationService.getNearbyStores(request);

        assertEquals("tienda-inexistente", response.getStoreName());
        assertEquals(0, response.getTotalEncontradas());
        assertTrue(response.getSucursales().isEmpty());
    }

    @Test
    void getNearbyStores_shouldFilterByRadius() {
        LocationRequest request = new LocationRequest();
        request.setStoreName("simermart");
        request.setLat(-33.5117);
        request.setLng(-70.7677);
        request.setRadiusKm(0.1);

        NearbyStoresResponse response =
                locationService.getNearbyStores(request);

        assertTrue(response.getSucursales().size() <= 1);
    }

    @Test
    void getAllNearbyStores_shouldReturnStoresFromMultipleChains() {
        List<NearbyStoresResponse> response =
                locationService.getAllNearbyStores(
                        -33.5117,
                        -70.7677,
                        50.0
                );

        assertNotNull(response);
        assertFalse(response.isEmpty());

        assertTrue(response.stream()
                .anyMatch(r -> !r.getSucursales().isEmpty()));
    }

    @Test
    void getAllNearbyStores_shouldReturnEmptyWhenRadiusIsTooSmall() {
        List<NearbyStoresResponse> response =
                locationService.getAllNearbyStores(
                        -90.0,
                        -180.0,
                        0.1
                );

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void searchStores_shouldThrowUnsupportedOperationException() {
        LocationRequest request = new LocationRequest();

        UnsupportedOperationException exception =
                assertThrows(
                        UnsupportedOperationException.class,
                        () -> locationService.searchStores(request)
                );

        assertTrue(exception.getMessage().contains("Unimplemented method"));
    }
}