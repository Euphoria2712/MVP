package ai_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI aiServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Service API")
                        .version("1.0")
                        .description("Microservicio encargado de procesar mensajes del usuario, detectar intenciones y generar respuestas con inteligencia artificial.")
                        .contact(new Contact()
                                .name("Byron Hinojosa")
                                .email("by.hinojosa@duocuc.cl")));
    }
}