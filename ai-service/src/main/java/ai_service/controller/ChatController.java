package ai_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ai_service.domain.Conversation;
import ai_service.dto.ChatRequest;
import ai_service.dto.ChatResponse;
import ai_service.service.ChatService;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/conversations/me")
    public ResponseEntity<List<Conversation>> getMyConversations(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(chatService.getMyConversations(userId));
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<Conversation> getConversationById(
            @PathVariable String id) {
        return ResponseEntity.ok(chatService.getConversationById(id));
    }

    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Void> deleteConversation(
            @PathVariable String id) {
        chatService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "Usuario") String userName,
            @RequestHeader(value = "X-User-Ciudad", defaultValue = "Santiago") String ciudad,
            @RequestHeader(value = "X-User-Supermercado", defaultValue = "simermart") String supermercadoFav,
            @Valid @RequestBody ChatRequest request) {

        return ResponseEntity.ok(
                chatService.chat(userId, userName,
                        ciudad, supermercadoFav, request));
    }
}