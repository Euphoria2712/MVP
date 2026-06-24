package user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import user_service.domain.Role;
import user_service.domain.User;
import user_service.domain.UserProfile;
import user_service.dto.LoginRequest;
import user_service.dto.LoginResponse;
import user_service.dto.RegisterRequest;
import user_service.dto.UserResponse;
import user_service.repository.UserProfileRepository;
import user_service.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldCreateUserAndProfile() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Catalina");
        request.setApellido("Riveros");
        request.setEmail("cata@test.com");
        request.setPassword("123456");
        request.setCiudad("Viña del Mar");
        request.setSupermercadoFav("Lider");

        when(userRepository.existsByEmail("cata@test.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("user-1");
            user.setRol(Role.USER);
            user.setActivo(true);
            return user;
        });

        when(profileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = authService.register(request);

        assertEquals("user-1", response.getId());
        assertEquals("Catalina", response.getNombre());
        assertEquals("Riveros", response.getApellido());
        assertEquals("cata@test.com", response.getEmail());
        assertEquals("USER", response.getRol());
        assertTrue(response.getActivo());

        verify(userRepository).existsByEmail("cata@test.com");
        verify(userRepository).save(any(User.class));
        verify(profileRepository).save(any(UserProfile.class));
    }

    @Test
    void register_shouldThrowWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("cata@test.com");

        when(userRepository.existsByEmail("cata@test.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("El email ya está registrado", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
        verify(profileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void login_shouldReturnTokenWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("cata@test.com");
        request.setPassword("123456");

        User user = User.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .password("encoded-password")
                .rol(Role.USER)
                .activo(true)
                .build();

        when(userRepository.findByEmail("cata@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "encoded-password"))
                .thenReturn(true);

        when(jwtService.generateToken("user-1", "cata@test.com", "USER"))
                .thenReturn("fake-token");

        when(jwtService.getExpiration())
                .thenReturn(86400000L);

        LoginResponse response = authService.login(request);

        assertEquals("fake-token", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertEquals("user-1", response.getUserId());
        assertEquals("Catalina", response.getNombre());
        assertEquals("cata@test.com", response.getEmail());
        assertEquals("USER", response.getRol());
    }

    @Test
    void login_shouldThrowWhenEmailDoesNotExist() {
        LoginRequest request = new LoginRequest();
        request.setEmail("noexiste@test.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("noexiste@test.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Credenciales incorrectas", exception.getMessage());

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString(), anyString(), anyString());
    }

    @Test
    void login_shouldThrowWhenUserIsInactive() {
        LoginRequest request = new LoginRequest();
        request.setEmail("cata@test.com");
        request.setPassword("123456");

        User user = User.builder()
                .id("user-1")
                .email("cata@test.com")
                .password("encoded-password")
                .rol(Role.USER)
                .activo(false)
                .build();

        when(userRepository.findByEmail("cata@test.com"))
                .thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Cuenta desactivada", exception.getMessage());

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString(), anyString(), anyString());
    }

    @Test
    void login_shouldThrowWhenPasswordIsInvalid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("cata@test.com");
        request.setPassword("bad-password");

        User user = User.builder()
                .id("user-1")
                .email("cata@test.com")
                .password("encoded-password")
                .rol(Role.USER)
                .activo(true)
                .build();

        when(userRepository.findByEmail("cata@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("bad-password", "encoded-password"))
                .thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Credenciales incorrectas", exception.getMessage());

        verify(jwtService, never()).generateToken(anyString(), anyString(), anyString());
    }
}