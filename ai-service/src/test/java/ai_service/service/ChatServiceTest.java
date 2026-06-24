package ai_service.service;

import ai_service.client.OpenRouterClient;
import ai_service.client.UserServiceClient;
import ai_service.domain.Conversation;
import ai_service.dto.ChatRequest;
import ai_service.dto.ChatResponse;
import ai_service.repository.ConversationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

        @Mock
        private OpenRouterClient openRouterClient;

        @Mock
        private UserServiceClient userServiceClient;

        @Mock
        private PromptService promptService;

        @Mock
        private ConversationRepository conversationRepository;

        @InjectMocks
        private ChatService chatService;

        private void mockBase(String rawResponse) {
                when(conversationRepository.findTop10ByUserIdOrderByCreatedAtDesc(any()))
                                .thenReturn(List.of());

                when(userServiceClient.getMemories(any()))
                                .thenReturn(List.of());

                when(promptService.buildHistorial(anyList()))
                                .thenReturn("historial");

                when(promptService.buildMemorias(anyList()))
                                .thenReturn("memorias");

                when(promptService.buildSystemPrompt(any(), any(), any(), any(), any()))
                                .thenReturn("system prompt");

                when(openRouterClient.sendMessage(any(), any()))
                                .thenReturn(rawResponse);

                when(conversationRepository.save(any()))
                                .thenAnswer(i -> {
                                        Conversation c = i.getArgument(0);
                                        c.setId("conv-1");
                                        return c;
                                });
        }

        @Test
        void getMyConversations_shouldReturnConversations() {
                Conversation conversation = Conversation.builder()
                                .id("conv-1")
                                .userId("user-1")
                                .userMessage("Hola")
                                .aiResponse("Hola")
                                .intentType("chat")
                                .build();

                when(conversationRepository.findByUserIdOrderByCreatedAtDesc("user-1"))
                                .thenReturn(List.of(conversation));

                List<Conversation> result = chatService.getMyConversations("user-1");

                assertEquals(1, result.size());
                assertEquals("conv-1", result.get(0).getId());
        }

        @Test
        void getConversationById_shouldReturnConversation() {
                Conversation conversation = Conversation.builder()
                                .id("conv-1")
                                .userId("user-1")
                                .build();

                when(conversationRepository.findById("conv-1"))
                                .thenReturn(Optional.of(conversation));

                Conversation result = chatService.getConversationById("conv-1");

                assertEquals("conv-1", result.getId());
        }

        @Test
        void getConversationById_shouldThrowWhenNotFound() {
                when(conversationRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(
                                RuntimeException.class,
                                () -> chatService.getConversationById("bad-id"));

                assertEquals("Conversación no encontrada", exception.getMessage());
        }

        @Test
        void deleteConversation_shouldDeleteConversation() {
                Conversation conversation = Conversation.builder()
                                .id("conv-1")
                                .build();

                when(conversationRepository.findById("conv-1"))
                                .thenReturn(Optional.of(conversation));

                chatService.deleteConversation("conv-1");

                verify(conversationRepository).delete(conversation);
        }

        @Test
        void deleteConversation_shouldThrowWhenNotFound() {
                when(conversationRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(RuntimeException.class,
                                () -> chatService.deleteConversation("bad-id"));

                verify(conversationRepository, never()).delete(any());
        }

        @Test
        void chat_shouldReturnChatResponse() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Hola");

                mockBase("{\"tipo\":\"chat\",\"respuesta\":\"Hola Catalina\"}");

                ChatResponse response = chatService.chat(
                                "user-1", "Catalina", "Viña", "Lider", request);

                assertEquals("chat", response.getIntentType());
                assertEquals("Hola Catalina", response.getRespuesta());
                assertEquals("conv-1", response.getConversacionId());
                assertNull(response.getDatos());
        }

        @Test
        void chat_shouldReturnRecipeResponse() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Quiero panqueques");

                mockBase("{\"tipo\":\"recipe\",\"receta\":\"Panqueques\",\"ingredientes\":[]}");

                ChatResponse response = chatService.chat(
                                "user-1", "Catalina", "Viña", "Lider", request);

                assertEquals("recipe", response.getIntentType());
                assertEquals("Panqueques", response.getRespuesta());
                assertNotNull(response.getDatos());
        }

        @Test
        void chat_shouldReturnSearchResponse() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("precio de leche");

                mockBase("{\"tipo\":\"search\",\"producto\":\"leche\"}");

                ChatResponse response = chatService.chat(
                                "user-1", "Catalina", "Viña", "Lider", request);

                assertEquals("search", response.getIntentType());
                assertEquals("leche", response.getRespuesta());
                assertNotNull(response.getDatos());
        }

        @Test
        void chat_shouldHandleWrappedRecipe() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("receta");

                mockBase("""
                                {
                                  "recipe":{
                                    "tipo":"recipe",
                                    "receta":"Torta"
                                  }
                                }
                                """);

                ChatResponse response = chatService.chat("u1", "cat", "vina", "lider", request);

                assertEquals("recipe", response.getIntentType());
                assertEquals("Torta", response.getRespuesta());
                assertNotNull(response.getDatos());
        }

        @Test
        void chat_shouldHandleWrappedSearch() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("leche");

                mockBase("""
                                {
                                  "search":{
                                    "tipo":"search",
                                    "producto":"Leche"
                                  }
                                }
                                """);

                ChatResponse response = chatService.chat("u1", "cat", "vina", "lider", request);

                assertEquals("search", response.getIntentType());
                assertEquals("Leche", response.getRespuesta());
                assertNotNull(response.getDatos());
        }

        @Test
        void chat_shouldHandleWrappedChat() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("hola");

                mockBase("""
                                {
                                  "chat":{
                                    "tipo":"chat",
                                    "respuesta":"Hola"
                                  }
                                }
                                """);

                ChatResponse response = chatService.chat("u1", "cat", "vina", "lider", request);

                assertEquals("chat", response.getIntentType());
                assertEquals("Hola", response.getRespuesta());
        }

        @Test
        void chat_shouldHandleMemoryClientError() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Hola");

                when(conversationRepository.findTop10ByUserIdOrderByCreatedAtDesc(any()))
                                .thenReturn(List.of());

                when(userServiceClient.getMemories(any()))
                                .thenThrow(new RuntimeException("user-service caído"));

                when(promptService.buildHistorial(anyList()))
                                .thenReturn("historial");

                when(promptService.buildMemorias(anyList()))
                                .thenReturn("memorias");

                when(promptService.buildSystemPrompt(any(), any(), any(), any(), any()))
                                .thenReturn("system prompt");

                when(openRouterClient.sendMessage(any(), any()))
                                .thenReturn("{\"tipo\":\"chat\",\"respuesta\":\"Hola igual\"}");

                when(conversationRepository.save(any()))
                                .thenAnswer(i -> {
                                        Conversation c = i.getArgument(0);
                                        c.setId("conv-4");
                                        return c;
                                });

                ChatResponse response = chatService.chat(
                                "user-1", "Catalina", "Viña", "Lider", request);

                assertEquals("chat", response.getIntentType());
                assertEquals("Hola igual", response.getRespuesta());
        }

        @Test
        void chat_shouldUsePlainTextWhenJsonParsingFails() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Hola");

                mockBase("texto plano sin json");

                ChatResponse response = chatService.chat(
                                "user-1", "Catalina", "Viña", "Lider", request);

                assertEquals("chat", response.getIntentType());
                assertEquals("texto plano sin json", response.getRespuesta());
        }

        @Test
        void chat_shouldFallbackWhenJsonParsingFails() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("hola");

                mockBase("{ json roto");

                ChatResponse response = chatService.chat("u1", "cat", "vina", "lider", request);

                assertEquals("chat", response.getIntentType());
        }

        @Test
        void chat_shouldCleanMarkdownAndThinkTags() {
                ChatRequest request = new ChatRequest();
                request.setMensaje("Hola");

                String raw = """
                                <think>razonamiento interno</think>
                                ```json
                                {"tipo":"chat","respuesta":"Hola limpio"}
                                ```
                                """;

                mockBase(raw);

                ChatResponse response = chatService.chat(
                                "user-1", "Catalina", "Viña", "Lider", request);

                assertEquals("chat", response.getIntentType());
                assertEquals("Hola limpio", response.getRespuesta());
        }

        @Test
        void extractJson_shouldHandleNull() throws Exception {
                Method method = ChatService.class
                                .getDeclaredMethod("extractJson", String.class);

                method.setAccessible(true);

                String result = (String) method.invoke(chatService, (String) null);

                assertEquals("{}", result);
        }

        @Test
        void extractJson_shouldRecoverAfterMarkdownRemoval() throws Exception {
                Method method = ChatService.class
                                .getDeclaredMethod("extractJson", String.class);

                method.setAccessible(true);

                String result = (String) method.invoke(
                                chatService,
                                "```json\n{}\n```");

                assertEquals("{}", result);
        }

        @Test
        void limpiarTexto_shouldHandleNull() throws Exception {
                Method method = ChatService.class
                                .getDeclaredMethod("limpiarTexto", String.class);

                method.setAccessible(true);

                String result = (String) method.invoke(chatService, (String) null);

                assertEquals("No pude procesar tu mensaje.", result);
        }
}