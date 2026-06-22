package intent_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient(name = "ai-service")
public interface AiServiceClient {

    @PostMapping("/api/chat")
    Map<String, Object> chat(
        @RequestHeader("X-User-Id")    String userId,
        @RequestHeader("X-User-Name")  String userName,
        @RequestBody                   Map<String, String> body
    );
}