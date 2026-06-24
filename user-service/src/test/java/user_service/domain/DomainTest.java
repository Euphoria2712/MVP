package user_service.domain;

import org.junit.jupiter.api.Test;
import user_service.domain.UserMemory.MemoryOrigin;

import static org.junit.jupiter.api.Assertions.*;

class DomainTest {

    @Test
    void userBuilder_shouldCreateUser() {
        User user = User.builder()
                .id("user-1")
                .nombre("Catalina")
                .apellido("Riveros")
                .email("cata@test.com")
                .password("123456")
                .rol(Role.USER)
                .activo(true)
                .build();

        assertEquals("user-1", user.getId());
        assertEquals("Catalina", user.getNombre());
        assertEquals("Riveros", user.getApellido());
        assertEquals("cata@test.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals(Role.USER, user.getRol());
        assertTrue(user.getActivo());
    }

    @Test
    void userProfileBuilder_shouldCreateProfile() {
        UserProfile profile = UserProfile.builder()
                .id("profile-1")
                .userId("user-1")
                .ciudad("Viña del Mar")
                .presupuesto("Medio")
                .supermercadoFav("Lider")
                .build();

        assertEquals("profile-1", profile.getId());
        assertEquals("user-1", profile.getUserId());
        assertEquals("Viña del Mar", profile.getCiudad());
        assertEquals("Medio", profile.getPresupuesto());
        assertEquals("Lider", profile.getSupermercadoFav());
    }

    @Test
    void userMemoryBuilder_shouldCreateMemory() {
        UserMemory memory = UserMemory.builder()
                .id("memory-1")
                .userId("user-1")
                .contenido("Memoria test")
                .origen(MemoryOrigin.USER_STATED)
                .relevancia(5)
                .build();

        assertEquals("memory-1", memory.getId());
        assertEquals("user-1", memory.getUserId());
        assertEquals("Memoria test", memory.getContenido());
        assertEquals(MemoryOrigin.USER_STATED, memory.getOrigen());
        assertEquals(5, memory.getRelevancia());
    }

    @Test
    void role_shouldContainUserAndAdmin() {
        assertNotNull(Role.USER);
        assertNotNull(Role.ADMIN);
    }

    @Test
    void memoryOrigin_shouldContainValues() {
        assertNotNull(MemoryOrigin.AUTO_EXTRACTED);
        assertNotNull(MemoryOrigin.USER_STATED);
    }
}