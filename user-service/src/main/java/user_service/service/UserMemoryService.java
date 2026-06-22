package user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_service.domain.UserMemory;
import user_service.repository.UserMemoryRepository;
import user_service.dto.UserMemoryRequest;
import user_service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMemoryService {

    private static final Logger log = LoggerFactory.getLogger(UserMemoryService.class);

    private final UserMemoryRepository memoryRepository;

    public List<UserMemory> findByUser(String userId) {

        log.info("Listando memorias para userId={}", userId);

        return memoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public UserMemory findById(String id) {

        log.info("Buscando memoria id={}", id);

        return memoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Memoria no encontrada id={}", id);
                    return new ResourceNotFoundException("Memoria no encontrada");
                });
    }

    @Transactional
    public UserMemory create(String userId, UserMemoryRequest request) {

        log.info("Creando memoria para userId={}", userId);

        UserMemory memory = UserMemory.builder()
                .userId(userId)
                .contenido(request.getContenido())
                .origen(request.getOrigen())
                .relevancia(request.getRelevancia())
                .build();

        UserMemory savedMemory = memoryRepository.save(memory);

        log.info("Memoria creada correctamente id={}", savedMemory.getId());

        return savedMemory;
    }

    @Transactional
    public UserMemory update(String id, UserMemoryRequest request) {

        log.info("Actualizando memoria id={}", id);

        UserMemory memory = memoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Memoria no encontrada id={}", id);
                    return new ResourceNotFoundException("Memoria no encontrada");
                });

        memory.setContenido(request.getContenido());
        memory.setOrigen(request.getOrigen());
        memory.setRelevancia(request.getRelevancia());

        UserMemory updatedMemory = memoryRepository.save(memory);

        log.info("Memoria actualizada correctamente id={}", id);

        return updatedMemory;
    }

    @Transactional
    public void delete(String id) {

        log.info("Eliminando memoria id={}", id);

        UserMemory memory = memoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Memoria no encontrada id={}", id);
                    return new ResourceNotFoundException("Memoria no encontrada");
                });

        memoryRepository.delete(memory);

        log.info("Memoria eliminada correctamente id={}", id);
    }
}