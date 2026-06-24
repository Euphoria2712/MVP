package response_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import response_service.dto.ChatRequest;
import response_service.dto.FinalResponse;
import response_service.service.ResponseService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseControllerTest {

    @Mock
    private ResponseService responseService;

    @InjectMocks
    private ResponseController responseController;

    @Test
    void process_shouldReturnFinalResponse() {

        ChatRequest request = new ChatRequest();
        request.setMensaje("Dame una receta");
        request.setUserLat(-33.0);
        request.setUserLng(-71.0);
        request.setRadiusKm(5.0);

        FinalResponse finalResponse = FinalResponse.builder()
                .tipo("recipe")
                .mensaje("Aquí tienes una receta")
                .conversacionId("conv-1")
                .build();

        when(responseService.process("user-1", request))
                .thenReturn(finalResponse);

        ResponseEntity<FinalResponse> response =
                responseController.process("user-1", request);

        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                "recipe",
                response.getBody().getTipo()
        );

        assertEquals(
                "Aquí tienes una receta",
                response.getBody().getMensaje()
        );

        assertEquals(
                "conv-1",
                response.getBody().getConversacionId()
        );

        verify(responseService)
                .process("user-1", request);
    }
}