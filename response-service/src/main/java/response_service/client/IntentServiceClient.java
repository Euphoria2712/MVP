package response_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient(name = "intent-service")
public interface IntentServiceClient {

    @PostMapping("/api/intent")
    Map<String, Object> process(
        @RequestHeader("X-User-Id") String userId,
        @RequestBody Map<String, Object> request
    );
}