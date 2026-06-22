package user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para registrar un nuevo usuario")
@Data
public class RegisterRequest {

    @Schema(description = "Nombre del usuario", example = "Catalina")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @Schema(description = "Apellido del usuario", example = "García")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @Schema(description = "Correo electrónico del usuario", example = "catalina@email.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "contraseña123")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Ciudad del usuario", example = "Madrid")
    private String ciudad;
    @Schema(description = "Presupuesto del usuario", example = "1000")
    private String presupuesto;
    @Schema(description = "Supermercado favorito del usuario", example = "Supermercado XYZ")
    private String supermercadoFav;
}