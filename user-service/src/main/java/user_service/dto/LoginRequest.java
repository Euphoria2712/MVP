package user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para iniciar sesión")
@Data
public class LoginRequest {

     @Schema(
        description = "Correo electrónico del usuario",
        example = "catalina@email.com"
    )
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Schema(
        description = "Contraseña del usuario",
        example = "contraseña123"
    )
    private String password;
}