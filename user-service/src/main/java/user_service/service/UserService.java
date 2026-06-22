package user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_service.domain.User;
import user_service.dto.UpdateUserRequest;
import user_service.dto.UserResponse;
import user_service.exception.ResourceNotFoundException;
import user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public List<UserResponse> findAll() {
        log.info("Listando todos los usuarios");

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(String id) {
        log.info("Buscando usuario con id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id={}", id);
                    return new ResourceNotFoundException("Usuario no encontrado");
                });

        return toResponse(user);
    }

    @Transactional
    public UserResponse update(String id, UpdateUserRequest request) {
        log.info("Actualizando usuario con id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Usuario no encontrado con id={}", id);
                    return new ResourceNotFoundException("Usuario no encontrado");
                });

        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setEmail(request.getEmail());
        user.setActivo(request.getActivo());

        User updatedUser = userRepository.save(user);

        log.info("Usuario actualizado correctamente con id={}", id);

        return toResponse(updatedUser);
    }

    @Transactional
    public void delete(String id) {
        log.info("Eliminando usuario con id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Usuario no encontrado con id={}", id);
                    return new ResourceNotFoundException("Usuario no encontrado");
                });

        userRepository.delete(user);

        log.info("Usuario eliminado correctamente con id={}", id);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .email(user.getEmail())
                .rol(user.getRol().name())
                .activo(user.getActivo())
                .build();
    }
}