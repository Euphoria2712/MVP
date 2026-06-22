package user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Schema(description = "Solicitud para actualizar el perfil del usuario")
public class UserProfileRequest {

    @Schema(description = "Ciudad del usuario", example = "Madrid")
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @Schema(description = "Intolerancias del usuario", example = "[\"Lactosa\", \"Gluten\"]")
    private List<String> intolerancias;

    @Schema(description = "Preferencias de cocina del usuario", example = "[\"Mediterránea\", " +
            "\"Italiana\"]")
    private List<String> prefCocina;

    @Schema(description = "Presupuesto del usuario", example = "500")
    @NotBlank(message = "El presupuesto es obligatorio")
    private String presupuesto;

    @Schema(description = "Supermercado favorito del usuario", example = "Supermercado XYZ")
    @NotBlank(message = "El supermercado favorito es obligatorio")
    private String supermercadoFav;
}