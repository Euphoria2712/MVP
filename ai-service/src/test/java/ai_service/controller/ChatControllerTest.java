package ai_service.controller;

import ai_service.domain.Conversation;
import ai_service.dto.ChatRequest;
import ai_service.dto.ChatResponse;
import ai_service.service.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    @Test
    void getMyConversations_shouldReturnConversations() {
        Conversation conversation = Conversation.builder()
                .id("conv-1")
                .userId("user-1")
                .userMessage("Hola")
                .aiResponse("Hola")
                .intentType("chat")
                .build();

        when(chatService.getMyConversations("user-1"))
                .thenReturn(List.of(conversation));

        ResponseEntity<List<Conversation>> response = chatController.getMyConversations("user-1");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("conv-1", response.getBody().get(0).getId());

        verify(chatService).getMyConversations("user-1");
    }

    @Test
    void getConversationById_shouldReturnConversation() {
        Conversation conversation = Conversation.builder()
                .id("conv-1")
                .userId("user-1")
                .build();

        when(chatService.getConversationById("conv-1"))
                .thenReturn(conversation);

        ResponseEntity<Conversation> response = chatController.getConversationById("conv-1");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("conv-1", response.getBody().getId());

        verify(chatService).getConversationById("conv-1");
    }

    @Test
    void deleteConversation_shouldReturnNoContent() {
        ResponseEntity<Void> response = chatController.deleteConversation("conv-1");

        assertEquals(204, response.getStatusCode().value());

        verify(chatService).deleteConversation("conv-1");
    }

    @Test
    void chat_shouldReturnChatResponse() {
        ChatRequest request = new ChatRequest();
        request.setMensaje("Hola");

        ChatResponse chatResponse = ChatResponse.builder()
                .respuesta("Hola Catalina")
                .intentType("chat")
                .conversacionId("conv-1")
                .build();

        when(chatService.chat(
                "user-1",
                "Catalina",
                "Viña",
                "Lider",
                request)).thenReturn(chatResponse);

        ResponseEntity<ChatResponse> response = chatController.chat(
                "user-1",
                "Catalina",
                "Viña",
                "Lider",
                request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("chat", response.getBody().getIntentType());
        assertEquals("Hola Catalina", response.getBody().getRespuesta());
        assertEquals("conv-1", response.getBody().getConversacionId());

        verify(chatService).chat(
                "user-1",
                "Catalina",
                "Viña",
                "Lider",
                request);
    }
}