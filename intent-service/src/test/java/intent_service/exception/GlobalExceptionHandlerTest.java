package intent_service.exception;

import intent_service.dto.ErrorResponse;
import intent_service.dto.ValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void handleValidation_shouldReturn400() {

        BindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "request");

        bindingResult.addError(
                new FieldError(
                        "request",
                        "mensaje",
                        "El mensaje es obligatorio"
                )
        );

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(
                        null,
                        bindingResult
                );

        ResponseEntity<ValidationErrorResponse> response =
                handler.handleValidation(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());

        assertEquals(400, response.getBody().getStatus());
        assertEquals("Error de validación", response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());

        assertEquals(
                "El mensaje es obligatorio",
                response.getBody().getMessages().get("mensaje")
        );
    }

    @Test
    void handleRuntime_shouldReturn400() {

        RuntimeException exception =
                new RuntimeException("Error de negocio");

        ResponseEntity<ErrorResponse> response =
                handler.handleRuntime(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());

        assertEquals(400, response.getBody().getStatus());
        assertEquals("Error de negocio", response.getBody().getError());
        assertEquals("Error de negocio", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}