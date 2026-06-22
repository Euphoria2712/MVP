package user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user_service.domain.UserProfile;
import user_service.dto.UserProfileRequest;
import user_service.exception.ResourceNotFoundException;
import user_service.repository.UserProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository profileRepository;

    public List<UserProfile> findAll() {
        log.info("Listando todos los perfiles");

        return profileRepository.findAll();
    }

    public UserProfile findById(String id) {
        log.info("Buscando perfil con id={}", id);

        return profileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Perfil no encontrado con id={}", id);
                    return new ResourceNotFoundException("Perfil no encontrado");
                });
    }

    public UserProfile findByUserId(String userId) {
        log.info("Buscando perfil para userId={}", userId);

        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Perfil no encontrado para userId={}", userId);
                    return new ResourceNotFoundException("Perfil no encontrado para el usuario");
                });
    }

    @Transactional
    public UserProfile create(String userId, UserProfileRequest request) {

        log.info("Creando perfil para userId={}", userId);

        if (profileRepository.findByUserId(userId).isPresent()) {
            log.warn("Intento de crear perfil duplicado para userId={}", userId);
            throw new RuntimeException("El usuario ya tiene un perfil");
        }

        UserProfile profile = UserProfile.builder()
                .userId(userId)
                .ciudad(request.getCiudad())
                .intolerancias(request.getIntolerancias())
                .prefCocina(request.getPrefCocina())
                .presupuesto(request.getPresupuesto())
                .supermercadoFav(request.getSupermercadoFav())
                .build();

        UserProfile savedProfile = profileRepository.save(profile);

        log.info("Perfil creado correctamente para userId={}", userId);

        return savedProfile;
    }

    @Transactional
    public UserProfile update(String id, UserProfileRequest profile2) {

        log.info("Actualizando perfil id={}", id);

        UserProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Perfil no encontrado id={}", id);
                    return new ResourceNotFoundException("Perfil no encontrado");
                });

        profile.setCiudad(profile2.getCiudad());
        profile.setIntolerancias(profile2.getIntolerancias());
        profile.setPrefCocina(profile2.getPrefCocina());
        profile.setPresupuesto(profile2.getPresupuesto());
        profile.setSupermercadoFav(profile2.getSupermercadoFav());

        UserProfile updatedProfile = profileRepository.save(profile);

        log.info("Perfil actualizado correctamente id={}", id);

        return updatedProfile;
    }

    @Transactional
    public void delete(String id) {

        log.info("Eliminando perfil id={}", id);

        UserProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Perfil no encontrado id={}", id);
                    return new ResourceNotFoundException("Perfil no encontrado");
                });

        profileRepository.delete(profile);

        log.info("Perfil eliminado correctamente id={}", id);
    }
}