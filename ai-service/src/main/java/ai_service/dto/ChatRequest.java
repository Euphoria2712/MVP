package ai_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Schema(description = "Request para el chat")
public class ChatRequest {

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(description = "Mensaje del usuario", example = "Hola, ¿cómo estás?")
    private String mensaje;
}