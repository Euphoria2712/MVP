package ai_service.service;

import ai_service.domain.Conversation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PromptServiceTest {

    private final PromptService promptService = new PromptService();

    @Test
    void buildSystemPrompt_shouldContainUserData() {
        String result = promptService.buildSystemPrompt(
                "Catalina",
                "Viña del Mar",
                "Lider",
                "Historial test",
                "Memoria test"
        );

        assertTrue(result.contains("Catalina"));
        assertTrue(result.contains("Viña del Mar"));
        assertTrue(result.contains("Lider"));
        assertTrue(result.contains("Historial test"));
        assertTrue(result.contains("Memoria test"));
        assertTrue(result.contains("\"tipo\":\"recipe\""));
        assertTrue(result.contains("\"tipo\":\"search\""));
        assertTrue(result.contains("\"tipo\":\"chat\""));
    }

    @Test
    void buildHistorial_shouldReturnDefaultWhenEmpty() {
        String result = promptService.buildHistorial(List.of());

        assertEquals("Sin historial previo.", result);
    }

    @Test
    void buildHistorial_shouldReturnFormattedHistory() {
        Conversation conversation = Conversation.builder()
                .userMessage("Hola")
                .aiResponse("Hola, ¿qué necesitas?")
                .build();

        String result = promptService.buildHistorial(List.of(conversation));

        assertTrue(result.contains("Usuario: Hola"));
        assertTrue(result.contains("Kuanto: Hola, ¿qué necesitas?"));
    }

    @Test
    void buildMemorias_shouldReturnDefaultWhenNull() {
        String result = promptService.buildMemorias(null);

        assertEquals("Sin memorias previas.", result);
    }

    @Test
    void buildMemorias_shouldReturnDefaultWhenEmpty() {
        String result = promptService.buildMemorias(List.of());

        assertEquals("Sin memorias previas.", result);
    }

    @Test
    void buildMemorias_shouldReturnFormattedMemories() {
        String result = promptService.buildMemorias(List.of("Le gusta Lider", "Vive en Viña"));

        assertTrue(result.contains("Le gusta Lider"));
        assertTrue(result.contains("Vive en Viña"));
    }
}