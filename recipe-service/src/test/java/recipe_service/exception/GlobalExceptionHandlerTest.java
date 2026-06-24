package recipe_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidation_shouldReturnValidationErrors() throws Exception {

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "recipe");

        bindingResult.addError(
                new FieldError(
                        "recipe",
                        "nombre",
                        "El nombre es obligatorio"));

        Method method = DummyController.class.getDeclaredMethod(
                "dummy",
                String.class);

        MethodParameter parameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                parameter,
                bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        assertEquals(400, response.getStatusCode().value());

        Map<String, Object> body = response.getBody();

        assertEquals(400, body.get("status"));
        assertEquals(
                "Error de validación",
                body.get("error"));

        Map<?, ?> messages = (Map<?, ?>) body.get("messages");

        assertEquals(
                "El nombre es obligatorio",
                messages.get("nombre"));
    }

    @Test
    void handleNotFound_shouldReturn404() {

        ResourceNotFoundException ex = new ResourceNotFoundException(
                "Receta no encontrada");

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());

        Map<String, Object> body = response.getBody();

        assertEquals(404, body.get("status"));
        assertEquals(
                "Recurso no encontrado",
                body.get("error"));
        assertEquals(
                "Receta no encontrada",
                body.get("message"));
    }

    @Test
    void handleRuntime_shouldReturn400() {

        RuntimeException ex = new RuntimeException(
                "Error de negocio");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertEquals(400, response.getStatusCode().value());

        Map<String, Object> body = response.getBody();

        assertEquals(400, body.get("status"));
        assertEquals(
                "Error de negocio",
                body.get("error"));
        assertEquals(
                "Error de negocio",
                body.get("message"));
    }

    static class DummyController {
        public void dummy(String value) {
        }
    }
}