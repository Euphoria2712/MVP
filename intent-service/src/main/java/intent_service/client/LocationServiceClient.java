package intent_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "location-service")
public interface LocationServiceClient {

    @GetMapping("/api/locations/nearby/all")
    List<Map<String, Object>> getAllNearby(
        @RequestParam("lat")      double lat,
        @RequestParam("lng")      double lng,
        @RequestParam("radiusKm") double radiusKm
    );
}