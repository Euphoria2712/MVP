package user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileRequest {

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    private List<String> intolerancias;

    private List<String> prefCocina;

    @NotBlank(message = "El presupuesto es obligatorio")
    private String presupuesto;

    @NotBlank(message = "El supermercado favorito es obligatorio")
    private String supermercadoFav;
}