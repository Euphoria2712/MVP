package ai_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ai_service.dto.OpenRouterRequest;
import ai_service.dto.OpenRouterResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenRouterClient {

    @Value("${openrouter.api-key}")
    private String apiKey;

    @Value("${openrouter.model}")
    private String model;

    @Value("${openrouter.url}")
    private String url;

    private final RestTemplate restTemplate;

    /**
     * Envía un mensaje a OpenRouter utilizando el modelo principal configurado.
     * Si falla o se satura (429/503/etc.), intenta automáticamente con un modelo de respaldo.
     */
    public String sendMessage(String systemPrompt, String userMessage) {
        try {
            log.info("Intentando llamada con modelo principal: {}", this.model);
            return callApi(this.model, systemPrompt, userMessage);
        } catch (Exception e) {
            log.warn("Modelo principal ({}) falló o está saturado: {}. Intentando fallback...", this.model, e.getMessage());
            try {
                // Modelo alternativo gratuito y rápido de OpenRouter
                String fallbackModel = "google/gemini-flash-1.5-8b"; 
                log.info("Llamando al modelo de respaldo: {}", fallbackModel);
                return callApi(fallbackModel, systemPrompt, userMessage);
            } catch (Exception ex) {
                log.error("Todos los intentos con OpenRouter fallaron de forma consecutiva: {}", ex.getMessage());
                // Retornamos un JSON por defecto para evitar que el parser del ChatService explote
                return "{\"tipo\":\"chat\",\"respuesta\":\"Disculpa, en este momento todos mis servidores están saturados. ¿Podrías intentar nuevamente en unos segundos?\"}";
            }
        }
    }

    /**
     * Realiza la llamada HTTP a OpenRouter para un modelo específico.
     */
    private String callApi(String targetModel, String systemPrompt, String userMessage) throws Exception {
        // Reducimos la temperatura a 0.5 para que la respuesta sea más estructurada, predecible y siga mejor el formato JSON.
        OpenRouterRequest request = OpenRouterRequest.builder()
            .model(targetModel)
            .max_tokens(1024)
            .temperature(0.5)
            .messages(List.of(
                new OpenRouterRequest.Message("system", systemPrompt),
                new OpenRouterRequest.Message("user",   userMessage)
            ))
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer",  "http://localhost:3000");
        headers.set("X-Title",       "Kuanto App");

        HttpEntity<OpenRouterRequest> entity = new HttpEntity<>(request, headers);

        // Puede lanzar excepciones de RestTemplate (Timeout, HttpStatusCodeException, etc.)
        OpenRouterResponse response = restTemplate.postForObject(url, entity, OpenRouterResponse.class);
        
        String text = response != null ? response.getText() : "";
        log.debug("Respuesta OpenRouter para modelo [{}]: {}", targetModel, text);

        // Limpia el bloque <think>...</think> si se utilizó un modelo con pensamiento (como DeepSeek R1)
        String cleaned = removeThinkingBlock(text);
        log.debug("Respuesta limpia final enviada al servicio: {}", cleaned);
        
        return cleaned;
    }

    /**
     * Remueve los bloques de razonamiento interno <think>...</think> producidos por modelos de razonamiento.
     */
    private String removeThinkingBlock(String text) {
        if (text == null) return "{}";
        
        // Remueve todo lo que esté entre las etiquetas <think> y </think>, incluyendo saltos de línea (?s)
        String cleaned = text.replaceAll("(?s)<think>.*?</think>", "").trim();
        
        // Si la respuesta quedó vacía debido a la limpieza, devolvemos un JSON básico de chat
        return cleaned.isEmpty() ? "{\"tipo\":\"chat\",\"respuesta\":\"No pude procesar tu mensaje de manera óptima.\"}" : cleaned;
    }
}