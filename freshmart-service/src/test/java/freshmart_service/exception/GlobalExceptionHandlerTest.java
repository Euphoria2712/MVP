package freshmart_service.exception;

import freshmart_service.dto.ErrorResponse;
import freshmart_service.dto.ValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidation_shouldReturn400() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");

        bindingResult.addError(
                new FieldError(
                        "request",
                        "nombre",
                        "El nombre es obligatorio"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ValidationErrorResponse> response = handler.handleValidation(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Error de validación", response.getBody().getError());
        assertEquals(
                "El nombre es obligatorio",
                response.getBody().getMessages().get("nombre"));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleNotFound_shouldReturn404() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Producto no encontrado");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(exception);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Recurso no encontrado", response.getBody().getError());
        assertEquals("Producto no encontrado", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleRuntime_shouldReturn400() {
        RuntimeException exception = new RuntimeException("Error de negocio");

        ResponseEntity<ErrorResponse> response = handler.handleRuntime(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Error de negocio", response.getBody().getError());
        assertEquals("Error de negocio", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleGeneralException_shouldReturn500() {
        Exception exception = new Exception("Error inesperado");

        ResponseEntity<ErrorResponse> response = handler.handleGeneralException(exception);

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Error interno del servidor", response.getBody().getError());
        assertEquals("Ocurrió un error inesperado", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void resourceNotFoundException_shouldStoreMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("No encontrado");

        assertEquals("No encontrado", exception.getMessage());
    }
}