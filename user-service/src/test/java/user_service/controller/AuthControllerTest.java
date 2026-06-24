package user_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import user_service.dto.LoginRequest;
import user_service.dto.LoginResponse;
import user_service.dto.RegisterRequest;
import user_service.dto.UserResponse;
import user_service.service.AuthService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_shouldReturnCreatedUser() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Catalina");
        request.setApellido("Riveros");
        request.setEmail("cata@test.com");
        request.setPassword("123456");

        UserResponse userResponse = UserResponse.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .rol("USER")
                .activo(true)
                .build();

        when(authService.register(request))
                .thenReturn(userResponse);

        ResponseEntity<UserResponse> response =
                authController.register(request);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("user-1", response.getBody().getId());
        assertEquals("Catalina", response.getBody().getNombre());

        verify(authService).register(request);
    }

    @Test
    void login_shouldReturnLoginResponse() {
        LoginRequest request = new LoginRequest();
        request.setEmail("cata@test.com");
        request.setPassword("123456");

        LoginResponse loginResponse = LoginResponse.builder()
                .token("fake-token")
                .tipo("Bearer")
                .userId("user-1")
                .nombre("Catalina")
                .email("cata@test.com")
                .rol("USER")
                .expira(86400000L)
                .build();

        when(authService.login(request))
                .thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response =
                authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("fake-token", response.getBody().getToken());
        assertEquals("Bearer", response.getBody().getTipo());

        verify(authService).login(request);
    }
}