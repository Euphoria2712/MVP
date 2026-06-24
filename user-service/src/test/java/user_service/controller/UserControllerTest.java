package user_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import user_service.domain.UserMemory;
import user_service.domain.UserMemory.MemoryOrigin;
import user_service.dto.UpdateUserRequest;
import user_service.dto.UserMemoryRequest;
import user_service.dto.UserResponse;
import user_service.service.UserMemoryService;
import user_service.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserMemoryService memoryService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void findAll_shouldReturnUsers() {
        UserResponse user = UserResponse.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .rol("USER")
                .activo(true)
                .build();

        when(userService.findAll()).thenReturn(List.of(user));

        ResponseEntity<List<UserResponse>> response = userController.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("user-1", response.getBody().get(0).getId());

        verify(userService).findAll();
    }

    @Test
    void findById_shouldReturnUser() {
        UserResponse user = UserResponse.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .rol("USER")
                .activo(true)
                .build();

        when(userService.findById("user-1")).thenReturn(user);

        ResponseEntity<UserResponse> response = userController.findById("user-1");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("user-1", response.getBody().getId());

        verify(userService).findById("user-1");
    }

    @Test
    void update_shouldReturnUpdatedUser() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setNombre("Catalina");
        request.setApellido("Riveros");
        request.setEmail("new@test.com");
        request.setActivo(true);

        UserResponse updated = UserResponse.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("new@test.com")
                .rol("USER")
                .activo(true)
                .build();

        when(userService.update("user-1", request)).thenReturn(updated);

        ResponseEntity<UserResponse> response =
                userController.update("user-1", request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("new@test.com", response.getBody().getEmail());

        verify(userService).update("user-1", request);
    }

    @Test
    void delete_shouldReturnNoContent() {
        ResponseEntity<Void> response = userController.delete("user-1");

        assertEquals(204, response.getStatusCode().value());

        verify(userService).delete("user-1");
    }

    @Test
    void getMe_shouldReturnAuthenticatedUser() {
        UserResponse user = UserResponse.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .rol("USER")
                .activo(true)
                .build();

        when(userService.findById("user-1")).thenReturn(user);

        ResponseEntity<UserResponse> response = userController.getMe("user-1");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("user-1", response.getBody().getId());

        verify(userService).findById("user-1");
    }

    @Test
    void getMemories_shouldReturnMemories() {
        UserMemory memory = UserMemory.builder()
                .id("memory-1")
                .userId("user-1")
                .contenido("Memoria test")
                .origen(MemoryOrigin.USER_STATED)
                .relevancia(5)
                .build();

        when(memoryService.findByUser("user-1"))
                .thenReturn(List.of(memory));

        ResponseEntity<List<UserMemory>> response =
                userController.getMemories("user-1");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("memory-1", response.getBody().get(0).getId());

        verify(memoryService).findByUser("user-1");
    }

    @Test
    void saveMemory_shouldReturnCreatedMemory() {
        UserMemoryRequest request = new UserMemoryRequest();
        request.setContenido("Nueva memoria");
        request.setOrigen(MemoryOrigin.USER_STATED);
        request.setRelevancia(5);

        UserMemory memory = UserMemory.builder()
                .id("memory-1")
                .userId("user-1")
                .contenido("Nueva memoria")
                .origen(MemoryOrigin.USER_STATED)
                .relevancia(5)
                .build();

        when(memoryService.create("user-1", request))
                .thenReturn(memory);

        ResponseEntity<UserMemory> response =
                userController.saveMemory("user-1", request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("memory-1", response.getBody().getId());

        verify(memoryService).create("user-1", request);
    }

    @Test
    void findMemoryById_shouldReturnMemory() {
        UserMemory memory = UserMemory.builder()
                .id("memory-1")
                .userId("user-1")
                .contenido("Memoria test")
                .origen(MemoryOrigin.USER_STATED)
                .relevancia(5)
                .build();

        when(memoryService.findById("memory-1"))
                .thenReturn(memory);

        ResponseEntity<UserMemory> response =
                userController.findMemoryById("memory-1");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("memory-1", response.getBody().getId());

        verify(memoryService).findById("memory-1");
    }

    @Test
    void updateMemory_shouldReturnUpdatedMemory() {
        UserMemoryRequest request = new UserMemoryRequest();
        request.setContenido("Memoria actualizada");
        request.setOrigen(MemoryOrigin.USER_STATED);
        request.setRelevancia(5);

        UserMemory memory = UserMemory.builder()
                .id("memory-1")
                .userId("user-1")
                .contenido("Memoria actualizada")
                .origen(MemoryOrigin.USER_STATED)
                .relevancia(5)
                .build();

        when(memoryService.update("memory-1", request))
                .thenReturn(memory);

        ResponseEntity<UserMemory> response =
                userController.updateMemory("memory-1", request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Memoria actualizada", response.getBody().getContenido());

        verify(memoryService).update("memory-1", request);
    }

    @Test
    void deleteMemory_shouldReturnNoContent() {
        ResponseEntity<Void> response =
                userController.deleteMemory("memory-1");

        assertEquals(204, response.getStatusCode().value());

        verify(memoryService).delete("memory-1");
    }
}