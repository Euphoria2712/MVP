package intent_service.controller;

import intent_service.dto.IntentRequest;
import intent_service.dto.IntentResponse;
import intent_service.service.IntentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntentControllerTest {

    @Mock
    private IntentService intentService;

    @InjectMocks
    private IntentController intentController;

    @Test
    void process_shouldReturnIntentResponse() {

        IntentRequest request = new IntentRequest();
        request.setMensaje("quiero una receta");

        IntentResponse intentResponse = IntentResponse.builder()
                .intentType("recipe")
                .respuesta("Receta encontrada")
                .conversacionId("conv-1")
                .build();

        when(intentService.process(request, "user-1"))
                .thenReturn(intentResponse);

        ResponseEntity<IntentResponse> response = intentController.process("user-1", request);

        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                "recipe",
                response.getBody().getIntentType());

        assertEquals(
                "Receta encontrada",
                response.getBody().getRespuesta());

        assertEquals(
                "conv-1",
                response.getBody().getConversacionId());

        verify(intentService)
                .process(request, "user-1");
    }
}