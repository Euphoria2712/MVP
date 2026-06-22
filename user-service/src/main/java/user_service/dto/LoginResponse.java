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
@Schema(description = "Respuesta del login con token JWT")
public class LoginResponse {

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    @Schema(description = "Tipo de token", example = "Bearer")
    private String tipo;
    @Schema(description = "ID del usuario", example = "12345")
    private String userId;
    @Schema(description = "Nombre del usuario", example = "Catalina")
    private String nombre;
    @Schema(description = "Correo electrónico del usuario", example = "catalina@email.com")
    private String email;
    @Schema(description = "Rol del usuario", example = "USER")
    private String rol;
    @Schema(description = "Timestamp de expiración del token", example = "1634567890")
    private long expira;
}