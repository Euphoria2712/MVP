package recipe_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI recipeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Recipe Service API")
                        .version("1.0")
                        .description("Microservicio encargado de gestionar recetas, incluyendo creación, actualización, eliminación y búsqueda de recetas.  ")
                        .contact(new Contact()
                                .name("Byron Hinojosa y Maximiliano Ramos")
                                .email("by.hinojosa@duocuc.cl")));
    }
}