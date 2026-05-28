package api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

            // user-service
            .route("user-service", r -> r
                .path("/api/auth/**", "/api/users/**", "/api/admin/**")
                .uri("lb://user-service"))   // lb:// = load balanced via Eureka

            // ai-service
            .route("ai-service", r -> r
                .path("/api/chat/**")
                .uri("lb://ai-service"))

            // recipe-service
            .route("recipe-service", r -> r
                .path("/api/recipes/**")
                .uri("lb://recipe-service"))

            // price-service
            .route("price-service", r -> r
                .path("/api/prices/**")
                .uri("lb://price-service"))

            // location-service
            .route("location-service", r -> r
                .path("/api/locations/**")
                .uri("lb://location-service"))

            // mock-store-service (solo dev)
            .route("freshmart-service", r -> r
                .path("/api/freshmart/**")
                .uri("lb://freshmart-service"))

            // intent-service
            .route("intent-service", r -> r
                .path("/api/intent/**")
                .uri("lb://intent-service"))

            
            .route("response-service", r -> r
                .path("/api/response/**")
                .uri("lb://response-service"))

            .build();
    }
}