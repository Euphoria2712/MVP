package user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "cuanto-super-secret-key-2024-development-only"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                86400000L
        );
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtService.generateToken(
                "user-1",
                "cata@test.com",
                "USER"
        );

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
    }

    @Test
    void extractUserId_shouldReturnSubject() {
        String token = jwtService.generateToken(
                "user-1",
                "cata@test.com",
                "USER"
        );

        String userId = jwtService.extractUserId(token);

        assertEquals("user-1", userId);
    }

    @Test
    void isValid_shouldReturnFalseWhenTokenIsInvalid() {
        boolean result = jwtService.isValid("token-falso");

        assertFalse(result);
    }

    @Test
    void getExpiration_shouldReturnConfiguredExpiration() {
        assertEquals(86400000L, jwtService.getExpiration());
    }
}