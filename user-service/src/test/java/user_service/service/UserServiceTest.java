package user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user_service.domain.Role;
import user_service.domain.User;
import user_service.dto.UpdateUserRequest;
import user_service.dto.UserResponse;
import user_service.exception.ResourceNotFoundException;
import user_service.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_shouldReturnUsers() {
        User user = User.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .rol(Role.USER)
                .activo(true)
                .build();

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserResponse> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("user-1", result.get(0).getId());
        assertEquals("Catalina", result.get(0).getNombre());
        assertEquals("Riveros", result.get(0).getApellido());
        assertEquals("cata@test.com", result.get(0).getEmail());
        assertEquals("USER", result.get(0).getRol());
        assertTrue(result.get(0).getActivo());
    }

    @Test
    void findById_shouldReturnUser() {
        User user = User.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .rol(Role.USER)
                .activo(true)
                .build();

        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(user));

        UserResponse result = userService.findById("user-1");

        assertEquals("user-1", result.getId());
        assertEquals("Catalina", result.getNombre());
        assertEquals("Riveros", result.getApellido());
        assertEquals("cata@test.com", result.getEmail());
        assertEquals("USER", result.getRol());
        assertTrue(result.getActivo());
    }

    @Test
    void findById_shouldThrowWhenUserNotFound() {
        when(userRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.findById("bad-id"));
    }

    @Test
    void update_shouldUpdateUser() {
        User user = User.builder()
                .id("user-1")
                .nombre("Cata")
                .apellido("Riveros")
                .email("old@test.com")
                .rol(Role.USER)
                .activo(true)
                .build();

        UpdateUserRequest request = new UpdateUserRequest();
        request.setNombre("Catalina");
        request.setApellido("Riveros");
        request.setEmail("new@test.com");
        request.setActivo(true);

        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse result = userService.update("user-1", request);

        assertEquals("user-1", result.getId());
        assertEquals("Catalina", result.getNombre());
        assertEquals("Riveros", result.getApellido());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("USER", result.getRol());
        assertTrue(result.getActivo());

        verify(userRepository).save(user);
    }

    @Test
    void update_shouldThrowWhenUserNotFound() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setNombre("Catalina");
        request.setApellido("Riveros");
        request.setEmail("new@test.com");
        request.setActivo(true);

        when(userRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.update("bad-id", request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void delete_shouldDeleteUser() {
        User user = User.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .rol(Role.USER)
                .activo(true)
                .build();

        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(user));

        userService.delete("user-1");

        verify(userRepository).delete(user);
    }

    @Test
    void delete_shouldThrowWhenUserNotFound() {
        when(userRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.delete("bad-id"));

        verify(userRepository, never()).delete(any(User.class));
    }
}