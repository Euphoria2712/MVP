package ai_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ai_service.client.OpenRouterClient;
import ai_service.client.UserServiceClient;
import ai_service.domain.Conversation;
import ai_service.dto.ChatRequest;
import ai_service.dto.ChatResponse;
import ai_service.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final OpenRouterClient       openRouterClient;
    private final UserServiceClient      userServiceClient;
    private final PromptService          promptService;
    private final ConversationRepository conversationRepository;
    private final ObjectMapper           objectMapper = new ObjectMapper();

    public ChatResponse chat(String userId, String userName,
                             String ciudad, String supermercadoFav,
                             ChatRequest request) {

        // 1. Historial de conversaciones
        List<Conversation> historial =
            conversationRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        // 2. Memorias del usuario desde user-service via Feign
        List<Object> memorias = List.of();
        try {
            memorias = userServiceClient.getMemories(userId);
        } catch (Exception e) {
            log.warn("No se pudieron cargar memorias: {}", e.getMessage());
        }

        // 3. Construye el prompt personalizado
        String systemPrompt = promptService.buildSystemPrompt(
            userName, ciudad, supermercadoFav,
            promptService.buildHistorial(historial),
            promptService.buildMemorias(memorias)
        );

        // 4. Llama a OpenRouter
        String rawResponse = openRouterClient.sendMessage(
            systemPrompt, request.getMensaje()
        );

        // 5. Intenta extraer JSON válido con múltiples estrategias de limpieza
        String jsonStr = extractJson(rawResponse);

        String intentType     = "chat";
        String respuestaTexto = rawResponse;
        Object datos          = null;

        try {
            JsonNode json = objectMapper.readTree(jsonStr);

            // ── DESEMPAQUETADO INTELIGENTE ─────────────────────────────────────────
            // Si la IA envolvió el objeto real dentro de un nodo raíz con el nombre del tipo
            if (json.has("recipe") && json.get("recipe").isObject()) {
                json = json.get("recipe");
            } else if (json.has("search") && json.get("search").isObject()) {
                json = json.get("search");
            } else if (json.has("chat") && json.get("chat").isObject()) {
                json = json.get("chat");
            }
            // ───────────────────────────────────────────────────────────────────────

            intentType = json.has("tipo")
                ? json.get("tipo").asText("chat")
                : "chat";

            if ("chat".equals(intentType)) {
                respuestaTexto = json.has("respuesta")
                    ? json.get("respuesta").asText()
                    : limpiarTexto(rawResponse);
            } else if ("recipe".equals(intentType)) {
                respuestaTexto = json.has("receta")
                    ? json.get("receta").asText()
                    : "Receta";
                datos = json;
            } else if ("search".equals(intentType)) {
                respuestaTexto = json.has("producto")
                    ? json.get("producto").asText()
                    : "Producto";
                datos = json;
            }

        } catch (Exception e) {
            log.warn("Parseo JSON falló, usando texto plano. Detalle: {}", e.getMessage());
            respuestaTexto = limpiarTexto(rawResponse);
            intentType     = "chat";
        }

        // 7. Guarda la conversación en MySQL
        Conversation conv = Conversation.builder()
            .userId(userId)
            .userMessage(request.getMensaje())
            .aiResponse(respuestaTexto)
            .intentType(intentType)
            .build();
        conv = conversationRepository.save(conv);

        return ChatResponse.builder()
            .respuesta(respuestaTexto)
            .intentType(intentType)
            .conversacionId(conv.getId())
            .datos(datos)
            .build();
    }

    // ── Estrategias de extracción de JSON ──────────────────────────────────────

    private String extractJson(String raw) {
        if (raw == null || raw.isBlank()) return "{}";

        // 1. Quita bloques de razonamiento <think>...</think>
        String cleaned = raw.replaceAll("(?s)<think>.*?</think>", "").trim();

        // 2. Quita bloques de formato markdown ```json ... ```
        cleaned = cleaned.replaceAll("(?s)```json\\s*", "")
                         .replaceAll("(?s)```\\s*", "")
                         .trim();

        // 3. Si quedó vacío tras la limpieza, intentamos recuperar el raw sin markdown
        if (cleaned.isBlank() || cleaned.equals("{}")) {
            cleaned = raw.replaceAll("(?s)```json\\s*", "")
                         .replaceAll("(?s)```\\s*", "")
                         .trim();
        }

        // 4. Busca la primera '{' y la última '}' para aislar estrictamente el objeto
        int start = cleaned.indexOf('{');
        int end   = cleaned.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            cleaned = cleaned.substring(start, end + 1);
        }

        // 5. Valida que el resultado final sea parseable, si no, devuelve un objeto vacío
        try {
            objectMapper.readTree(cleaned);
            return cleaned;
        } catch (Exception e) {
            log.warn("No se pudo extraer JSON estructurado válido del raw original");
            return "{}";
        }
    }

    // Limpia el texto residual para mostrarlo como un mensaje de chat normal si todo falla
    private String limpiarTexto(String raw) {
        if (raw == null) return "No pude procesar tu mensaje.";
        return raw.replaceAll("(?s)<think>.*?</think>", "")
                  .replaceAll("```json", "")
                  .replaceAll("```", "")
                  .replaceAll("\\{\\}", "")
                  .trim();
    }
}