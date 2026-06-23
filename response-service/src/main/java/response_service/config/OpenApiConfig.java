package response_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI responseServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Response Service API")
                        .version("1.0")
                        .description("Microservicio encargado de construir la respuesta final que recibe el usuario.")
                        .contact(new Contact()
                                .name("Byron Hinojosa")
                                .email("by.hinojosa@duocuc.cl")));
    }
}