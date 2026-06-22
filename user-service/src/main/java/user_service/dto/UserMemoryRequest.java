package user_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import user_service.domain.UserMemory.MemoryOrigin;

@Data
public class UserMemoryRequest {

    @NotBlank(message = "El contenido de la memoria es obligatorio")
    private String contenido;

    private MemoryOrigin origen;

    @Min(value = 1, message = "La relevancia mínima es 1")
    @Max(value = 5, message = "La relevancia máxima es 5")
    private Integer relevancia;
}