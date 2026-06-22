package user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con los datos del usuario")
public class UserResponse {

    @Schema(description = "ID del usuario", example = "12345")
    private String id;
    @Schema(description = "Nombre del usuario", example = "Catalina")
    private String nombre;
    @Schema(description = "Apellido del usuario", example = "García")
    private String apellido;
    @Schema(description = "Correo electrónico del usuario", example = "catalina@email.com")
    private String email;
    @Schema(description = "Rol del usuario", example = "USER")
    private String rol;
    @Schema(description = "Indica si el usuario está activo", example = "true")
    private Boolean activo;
}
