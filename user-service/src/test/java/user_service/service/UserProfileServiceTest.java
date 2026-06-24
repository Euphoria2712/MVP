package user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user_service.domain.UserProfile;
import user_service.dto.UserProfileRequest;
import user_service.exception.ResourceNotFoundException;
import user_service.repository.UserProfileRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository profileRepository;

    @InjectMocks
    private UserProfileService profileService;

    @Test
    void findAll_shouldReturnProfiles() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .prefCocina(List.of("Casera"))
                .presupuesto("Medio")
                .supermercadoFav("Lider")
                .build();

        when(profileRepository.findAll())
                .thenReturn(List.of(profile));

        List<UserProfile> result = profileService.findAll();

        assertEquals(1, result.size());
        assertEquals("profile-1", result.get(0).getId());
        assertEquals("user-1", result.get(0).getUserId());
    }

    @Test
    void findById_shouldReturnProfile() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .build();

        when(profileRepository.findById("profile-1"))
                .thenReturn(Optional.of(profile));

        UserProfile result = profileService.findById("profile-1");

        assertEquals("profile-1", result.getId());
        assertEquals("user-1", result.getUserId());
    }

    @Test
    void findById_shouldThrowWhenProfileNotFound() {
        when(profileRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.findById("bad-id"));
    }

    @Test
    void findByUserId_shouldReturnProfile() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .build();

        when(profileRepository.findByUserId("user-1"))
                .thenReturn(Optional.of(profile));

        UserProfile result = profileService.findByUserId("user-1");

        assertEquals("profile-1", result.getId());
        assertEquals("user-1", result.getUserId());
    }

    @Test
    void findByUserId_shouldThrowWhenProfileNotFound() {
        when(profileRepository.findByUserId("bad-user"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.findByUserId("bad-user"));
    }

    @Test
    void create_shouldCreateProfile() {
        UserProfileRequest request = new UserProfileRequest();
        request.setCiudad("Viña del Mar");
        request.setIntolerancias(List.of("Lactosa"));
        request.setPrefCocina(List.of("Casera"));
        request.setPresupuesto("Medio");
        request.setSupermercadoFav("Lider");

        when(profileRepository.findByUserId("user-1"))
                .thenReturn(Optional.empty());

        when(profileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> {
                    UserProfile profile = invocation.getArgument(0);
                    profile.setId("profile-1");
                    return profile;
                });

        UserProfile result = profileService.create("user-1", request);

        assertEquals("profile-1", result.getId());
        assertEquals("user-1", result.getUserId());
        assertEquals("Viña del Mar", result.getCiudad());
        assertEquals(List.of("Lactosa"), result.getIntolerancias());
        assertEquals(List.of("Casera"), result.getPrefCocina());
        assertEquals("Medio", result.getPresupuesto());
        assertEquals("Lider", result.getSupermercadoFav());

        verify(profileRepository).save(any(UserProfile.class));
    }

    @Test
    void create_shouldThrowWhenProfileAlreadyExists() {
        UserProfileRequest request = new UserProfileRequest();

        UserProfile existingProfile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .build();

        when(profileRepository.findByUserId("user-1"))
                .thenReturn(Optional.of(existingProfile));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> profileService.create("user-1", request));

        assertEquals("El usuario ya tiene un perfil", exception.getMessage());

        verify(profileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void update_shouldUpdateProfile() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Antigua")
                .intolerancias(List.of("Ninguna"))
                .prefCocina(List.of("Rapida"))
                .presupuesto("Bajo")
                .supermercadoFav("Otro")
                .build();

        UserProfileRequest request = new UserProfileRequest();
        request.setCiudad("Viña del Mar");
        request.setIntolerancias(List.of("Lactosa"));
        request.setPrefCocina(List.of("Casera"));
        request.setPresupuesto("Medio");
        request.setSupermercadoFav("Lider");

        when(profileRepository.findById("profile-1"))
                .thenReturn(Optional.of(profile));

        when(profileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile result = profileService.update("profile-1", request);

        assertEquals("profile-1", result.getId());
        assertEquals("Viña del Mar", result.getCiudad());
        assertEquals(List.of("Lactosa"), result.getIntolerancias());
        assertEquals(List.of("Casera"), result.getPrefCocina());
        assertEquals("Medio", result.getPresupuesto());
        assertEquals("Lider", result.getSupermercadoFav());

        verify(profileRepository).save(profile);
    }

    @Test
    void update_shouldThrowWhenProfileNotFound() {
        UserProfileRequest request = new UserProfileRequest();

        when(profileRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.update("bad-id", request));

        verify(profileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void delete_shouldDeleteProfile() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .build();

        when(profileRepository.findById("profile-1"))
                .thenReturn(Optional.of(profile));

        profileService.delete("profile-1");

        verify(profileRepository).delete(profile);
    }

    @Test
    void delete_shouldThrowWhenProfileNotFound() {
        when(profileRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.delete("bad-id"));

        verify(profileRepository, never()).delete(any(UserProfile.class));
    }
}