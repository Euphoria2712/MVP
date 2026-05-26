package ai_service.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(3)) // Máximo 3 segundos para establecer conexión TCP
            .setReadTimeout(Duration.ofSeconds(10))   // Máximo 10 segundos esperando que la IA responda
            .build();
    }
}