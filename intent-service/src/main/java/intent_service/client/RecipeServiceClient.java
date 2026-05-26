package intent_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "recipe-service")
public interface RecipeServiceClient {

    @GetMapping("/api/recipes/search")
    List<Map<String, Object>> search(@RequestParam("q") String query);
}