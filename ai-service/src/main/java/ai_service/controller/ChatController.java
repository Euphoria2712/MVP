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
import ai_service.dto.ErrorResponse;
import ai_service.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Operaciones relacionadas con el chat")
public class ChatController {

        private final ChatService chatService;

        @Operation(summary = "Obtener conversaciones del usuario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de conversaciones obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Conversation.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/conversations/me")
        public ResponseEntity<List<Conversation>> getMyConversations(
                        @RequestHeader("X-User-Id") String userId) {
                return ResponseEntity.ok(chatService.getMyConversations(userId));
        }

        @Operation(summary = "Obtener conversación por ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Conversación obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Conversation.class))),
                        @ApiResponse(responseCode = "404", description = "Conversación no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/conversations/{id}")
        public ResponseEntity<Conversation> getConversationById(
                        @PathVariable String id) {
                return ResponseEntity.ok(chatService.getConversationById(id));
        }

        @Operation(summary = "Eliminar conversación por ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Conversación eliminada exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Conversación no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @DeleteMapping("/conversations/{id}")
        public ResponseEntity<Void> deleteConversation(
                        @PathVariable String id) {
                chatService.deleteConversation(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Enviar mensaje al chat")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Mensaje enviado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
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