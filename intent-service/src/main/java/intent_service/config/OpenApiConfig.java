package intent_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI intentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Intent Service API")
                        .version("1.0")
                        .description("Microservicio encargado de interpretar la intención del usuario y coordinar respuestas según el mensaje recibido.")
                        .contact(new Contact()
                                .name("Byron Hinojosa")
                                .email("by.hinojosa@duocuc.cl")));
    }
}