package user_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import user_service.domain.UserProfile;
import user_service.dto.UserProfileRequest;
import user_service.service.UserProfileService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private UserProfileService profileService;

    @InjectMocks
    private UserProfileController profileController;

    @Test
    void findAll_shouldReturnProfiles() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .intolerancias(List.of("Lactosa"))
                .prefCocina(List.of("Casera"))
                .presupuesto("Medio")
                .supermercadoFav("Lider")
                .build();

        when(profileService.findAll())
                .thenReturn(List.of(profile));

        ResponseEntity<List<UserProfile>> response =
                profileController.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("profile-1", response.getBody().get(0).getId());

        verify(profileService).findAll();
    }

    @Test
    void findById_shouldReturnProfile() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .build();

        when(profileService.findById("profile-1"))
                .thenReturn(profile);

        ResponseEntity<UserProfile> response =
                profileController.findById("profile-1");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("profile-1", response.getBody().getId());

        verify(profileService).findById("profile-1");
    }

    @Test
    void findMyProfile_shouldReturnProfile() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .build();

        when(profileService.findByUserId("user-1"))
                .thenReturn(profile);

        ResponseEntity<UserProfile> response =
                profileController.findMyProfile("user-1");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("user-1", response.getBody().getUserId());

        verify(profileService).findByUserId("user-1");
    }

    @Test
    void createMyProfile_shouldReturnCreatedProfile() {
        UserProfileRequest request = new UserProfileRequest();
        request.setCiudad("Viña del Mar");
        request.setIntolerancias(List.of("Lactosa"));
        request.setPrefCocina(List.of("Casera"));
        request.setPresupuesto("Medio");
        request.setSupermercadoFav("Lider");

        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .intolerancias(List.of("Lactosa"))
                .prefCocina(List.of("Casera"))
                .presupuesto("Medio")
                .supermercadoFav("Lider")
                .build();

        when(profileService.create("user-1", request))
                .thenReturn(profile);

        ResponseEntity<UserProfile> response =
                profileController.createMyProfile("user-1", request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("profile-1", response.getBody().getId());
        assertEquals("Viña del Mar", response.getBody().getCiudad());

        verify(profileService).create("user-1", request);
    }

    @Test
    void update_shouldReturnUpdatedProfile() {
        UserProfileRequest request = new UserProfileRequest();
        request.setCiudad("Santiago");
        request.setIntolerancias(List.of("Gluten"));
        request.setPrefCocina(List.of("Italiana"));
        request.setPresupuesto("Alto");
        request.setSupermercadoFav("Jumbo");

        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Santiago")
                .intolerancias(List.of("Gluten"))
                .prefCocina(List.of("Italiana"))
                .presupuesto("Alto")
                .supermercadoFav("Jumbo")
                .build();

        when(profileService.update("profile-1", request))
                .thenReturn(profile);

        ResponseEntity<UserProfile> response =
                profileController.update("profile-1", request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Santiago", response.getBody().getCiudad());
        assertEquals("Jumbo", response.getBody().getSupermercadoFav());

        verify(profileService).update("profile-1", request);
    }

    @Test
    void delete_shouldReturnNoContent() {
        ResponseEntity<Void> response =
                profileController.delete("profile-1");

        assertEquals(204, response.getStatusCode().value());

        verify(profileService).delete("profile-1");
    }
}