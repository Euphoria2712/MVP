package freshmart_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI freshmartServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Freshmart Service API")
                        .version("1.0")
                        .description("Microservicio encargado de la gestión de productos y comparación de precios entre supermercados.")
                        .contact(new Contact()
                                .name("Byron Hinojosa")
                                .email("by.hinojosa@duocuc.cl")));
    }
}