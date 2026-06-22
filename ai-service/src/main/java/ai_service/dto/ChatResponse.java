package ai_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response del chat")
public class ChatResponse {

    @Schema(description = "Respuesta del chat", example = "Estoy bien, gracias. ¿Y tú?")
    private String respuesta;

    @Schema(description = "Tipo de intención", example = "saludo")
    private String intentType;

    @Schema(description = "ID de la conversación", example = "12345")
    private String conversacionId;

    @Schema(description = "Datos adicionales")
    private Object datos;
}