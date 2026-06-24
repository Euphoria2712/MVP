package user_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import user_service.dto.ErrorResponse;
import user_service.dto.ValidationErrorResponse;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_shouldReturn404() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Usuario no encontrado");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(exception);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Recurso no encontrado", response.getBody().getError());
        assertEquals("Usuario no encontrado", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleRuntime_shouldReturn400() {
        RuntimeException exception = new RuntimeException("El email ya está registrado");

        ResponseEntity<ErrorResponse> response = handler.handleRuntime(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Error de negocio", response.getBody().getError());
        assertEquals("El email ya está registrado", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void resourceNotFoundException_shouldStoreMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Memoria no encontrada");

        assertEquals("Memoria no encontrada", exception.getMessage());
    }

    @Test
    void handleValidation_shouldReturn400WithFieldErrors() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        org.springframework.validation.BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(
                new Object(),
                "request");

        bindingResult.addError(
                new org.springframework.validation.FieldError(
                        "request",
                        "email",
                        "El email no tiene formato válido"));

        bindingResult.addError(
                new org.springframework.validation.FieldError(
                        "request",
                        "password",
                        "La contraseña es obligatoria"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ValidationErrorResponse> response = handler.handleValidation(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Error de validación", response.getBody().getError());
        assertEquals("El email no tiene formato válido",
                response.getBody().getMessages().get("email"));
        assertEquals("La contraseña es obligatoria",
                response.getBody().getMessages().get("password"));
        assertNotNull(response.getBody().getTimestamp());
    }
}