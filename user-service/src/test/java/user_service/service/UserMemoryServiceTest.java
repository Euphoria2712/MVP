package user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user_service.domain.UserMemory;
import user_service.domain.UserMemory.MemoryOrigin;
import user_service.dto.UserMemoryRequest;
import user_service.exception.ResourceNotFoundException;
import user_service.repository.UserMemoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMemoryServiceTest {

        @Mock
        private UserMemoryRepository memoryRepository;

        @InjectMocks
        private UserMemoryService memoryService;

        @Test
        void findByUser_shouldReturnMemories() {
                UserMemory memory = UserMemory.builder()
                                .id("memory-1")
                                .userId("user-1")
                                .contenido("Compra en Lider")
                                .origen(MemoryOrigin.USER_STATED)
                                .relevancia(5)
                                .build();

                when(memoryRepository.findByUserIdOrderByCreatedAtDesc("user-1"))
                                .thenReturn(List.of(memory));

                List<UserMemory> result = memoryService.findByUser("user-1");

                assertEquals(1, result.size());
                assertEquals("memory-1", result.get(0).getId());
                assertEquals("user-1", result.get(0).getUserId());
        }

        @Test
        void findById_shouldReturnMemory() {
                UserMemory memory = UserMemory.builder()
                                .id("memory-1")
                                .userId("user-1")
                                .contenido("Compra en Lider")
                                .origen(MemoryOrigin.USER_STATED)
                                .relevancia(5)
                                .build();

                when(memoryRepository.findById("memory-1"))
                                .thenReturn(Optional.of(memory));

                UserMemory result = memoryService.findById("memory-1");

                assertEquals("memory-1", result.getId());
                assertEquals("user-1", result.getUserId());
                assertEquals("Compra en Lider", result.getContenido());
                assertEquals(MemoryOrigin.USER_STATED, result.getOrigen());
                assertEquals(5, result.getRelevancia());
        }

        @Test
        void findById_shouldThrowWhenMemoryNotFound() {
                when(memoryRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> memoryService.findById("bad-id"));
        }

        @Test
        void create_shouldCreateMemory() {
                UserMemoryRequest request = new UserMemoryRequest();
                request.setContenido("Le gusta comprar en Lider");
                request.setOrigen(MemoryOrigin.USER_STATED);
                request.setRelevancia(5);

                when(memoryRepository.save(any(UserMemory.class)))
                                .thenAnswer(invocation -> {
                                        UserMemory memory = invocation.getArgument(0);
                                        memory.setId("memory-1");
                                        return memory;
                                });

                UserMemory result = memoryService.create("user-1", request);

                assertEquals("memory-1", result.getId());
                assertEquals("user-1", result.getUserId());
                assertEquals("Le gusta comprar en Lider", result.getContenido());
                assertEquals(MemoryOrigin.USER_STATED, result.getOrigen());
                assertEquals(5, result.getRelevancia());

                verify(memoryRepository).save(any(UserMemory.class));
        }

        @Test
        void update_shouldUpdateMemory() {
                UserMemory memory = UserMemory.builder()
                                .id("memory-1")
                                .userId("user-1")
                                .contenido("Contenido antiguo")
                                .origen(MemoryOrigin.AUTO_EXTRACTED)
                                .relevancia(1)
                                .build();

                UserMemoryRequest request = new UserMemoryRequest();
                request.setContenido("Contenido actualizado");
                request.setOrigen(MemoryOrigin.USER_STATED);
                request.setRelevancia(5);

                when(memoryRepository.findById("memory-1"))
                                .thenReturn(Optional.of(memory));

                when(memoryRepository.save(any(UserMemory.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                UserMemory result = memoryService.update("memory-1", request);

                assertEquals("memory-1", result.getId());
                assertEquals("Contenido actualizado", result.getContenido());
                assertEquals(MemoryOrigin.USER_STATED, result.getOrigen());
                assertEquals(5, result.getRelevancia());

                verify(memoryRepository).save(memory);
        }

        @Test
        void update_shouldThrowWhenMemoryNotFound() {
                UserMemoryRequest request = new UserMemoryRequest();
                request.setContenido("Contenido actualizado");
                request.setOrigen(MemoryOrigin.USER_STATED);
                request.setRelevancia(5);

                when(memoryRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> memoryService.update("bad-id", request));

                verify(memoryRepository, never()).save(any(UserMemory.class));
        }

        @Test
        void delete_shouldDeleteMemory() {
                UserMemory memory = UserMemory.builder()
                                .id("memory-1")
                                .userId("user-1")
                                .build();

                when(memoryRepository.findById("memory-1"))
                                .thenReturn(Optional.of(memory));

                memoryService.delete("memory-1");

                verify(memoryRepository).delete(memory);
        }

        @Test
        void delete_shouldThrowWhenMemoryNotFound() {
                when(memoryRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> memoryService.delete("bad-id"));

                verify(memoryRepository, never()).delete(any(UserMemory.class));
        }
}