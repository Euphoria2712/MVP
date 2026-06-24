package ai_service.client;

import ai_service.dto.OpenRouterResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OpenRouterClientTest {

    @Test
    void sendMessage_shouldReturnCleanResponse() {
        RestTemplate restTemplate = mock(RestTemplate.class);

        OpenRouterClient client = new OpenRouterClient(restTemplate);

        ReflectionTestUtils.setField(client, "apiKey", "fake-key");
        ReflectionTestUtils.setField(client, "model", "main-model");
        ReflectionTestUtils.setField(client, "url", "http://fake-url");

        OpenRouterResponse.Message message = new OpenRouterResponse.Message();
        message.setContent("<think>razonamiento</think>{\"tipo\":\"chat\",\"respuesta\":\"Hola\"}");

        OpenRouterResponse.Choice choice = new OpenRouterResponse.Choice();
        choice.setMessage(message);

        OpenRouterResponse openRouterResponse = new OpenRouterResponse();
        openRouterResponse.setChoices(List.of(choice));

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(OpenRouterResponse.class))).thenReturn(openRouterResponse);

        String result = client.sendMessage("system", "hola");

        assertEquals("{\"tipo\":\"chat\",\"respuesta\":\"Hola\"}", result);
    }

    @Test
    void sendMessage_shouldUseFallbackWhenMainModelFails() {
        RestTemplate restTemplate = mock(RestTemplate.class);

        OpenRouterClient client = new OpenRouterClient(restTemplate);

        ReflectionTestUtils.setField(client, "apiKey", "fake-key");
        ReflectionTestUtils.setField(client, "model", "main-model");
        ReflectionTestUtils.setField(client, "url", "http://fake-url");

        OpenRouterResponse.Message message = new OpenRouterResponse.Message();
        message.setContent("{\"tipo\":\"chat\",\"respuesta\":\"Fallback ok\"}");

        OpenRouterResponse.Choice choice = new OpenRouterResponse.Choice();
        choice.setMessage(message);

        OpenRouterResponse openRouterResponse = new OpenRouterResponse();
        openRouterResponse.setChoices(List.of(choice));

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(OpenRouterResponse.class)))
                .thenThrow(new RuntimeException("main fail"))
                .thenReturn(openRouterResponse);

        String result = client.sendMessage("system", "hola");

        assertEquals("{\"tipo\":\"chat\",\"respuesta\":\"Fallback ok\"}", result);

        verify(restTemplate, times(2))
                .postForObject(anyString(), any(), eq(OpenRouterResponse.class));
    }

    @Test
    void sendMessage_shouldReturnDefaultWhenAllModelsFail() {
        RestTemplate restTemplate = mock(RestTemplate.class);

        OpenRouterClient client = new OpenRouterClient(restTemplate);

        ReflectionTestUtils.setField(client, "apiKey", "fake-key");
        ReflectionTestUtils.setField(client, "model", "main-model");
        ReflectionTestUtils.setField(client, "url", "http://fake-url");

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(OpenRouterResponse.class))).thenThrow(new RuntimeException("fail"));

        String result = client.sendMessage("system", "hola");

        assertTrue(result.contains("servidores están saturados"));
    }

    @Test
    void sendMessage_shouldReturnDefaultWhenResponseIsNull() {
        RestTemplate restTemplate = mock(RestTemplate.class);

        OpenRouterClient client = new OpenRouterClient(restTemplate);

        ReflectionTestUtils.setField(client, "apiKey", "fake-key");
        ReflectionTestUtils.setField(client, "model", "main-model");
        ReflectionTestUtils.setField(client, "url", "http://fake-url");

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(OpenRouterResponse.class))).thenReturn(null);

        String result = client.sendMessage("system", "hola");

        assertEquals("{\"tipo\":\"chat\",\"respuesta\":\"No pude procesar tu mensaje de manera óptima.\"}", result);
    }
}