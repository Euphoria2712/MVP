package user_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import user_service.domain.UserMemory.MemoryOrigin;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Solicitud para agregar o actualizar una memoria del usuario")
public class UserMemoryRequest {

    @Schema(description = "Contenido de la memoria", example = "Recuerdo que me gusta la comida italiana")
    @NotBlank(message = "El contenido de la memoria es obligatorio")
    private String contenido;

    @Schema(description = "Origen de la memoria", example = "USER")
    private MemoryOrigin origen;

    @Schema(description = "Relevancia de la memoria", example = "5")
    @Min(value = 1, message = "La relevancia mínima es 1")
    @Max(value = 5, message = "La relevancia máxima es 5")
    private Integer relevancia;
}