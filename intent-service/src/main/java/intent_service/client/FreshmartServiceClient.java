package intent_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import intent_service.dto.PriceData;
import java.util.List;

@FeignClient(name = "freshmart-service")
public interface FreshmartServiceClient {

    @GetMapping("/api/freshmart/prices")
    List<PriceData> getPrices(@RequestParam("q") String query);
}