package user_service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_service.domain.User;
import user_service.domain.UserProfile;
import user_service.dto.LoginRequest;
import user_service.dto.LoginResponse;
import user_service.dto.RegisterRequest;
import user_service.dto.UserResponse;
import user_service.repository.UserProfileRepository;
import user_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {

        log.info("Intentando registrar usuario con email={}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registro rechazado. Email ya registrado={}", request.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }

        User user = User.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .userId(user.getId())
                .ciudad(request.getCiudad())
                .presupuesto(request.getPresupuesto())
                .supermercadoFav(request.getSupermercadoFav())
                .build();

        profileRepository.save(profile);

        log.info("Usuario registrado correctamente id={}, email={}", user.getId(), user.getEmail());

        return toResponse(user);
    }

    public LoginResponse login(LoginRequest request) {

        log.info("Intentando login con email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login rechazado. Email no encontrado={}", request.getEmail());
                    return new RuntimeException("Credenciales incorrectas");
                });

        if (!user.getActivo()) {
            log.warn("Login rechazado. Cuenta desactivada userId={}", user.getId());
            throw new RuntimeException("Cuenta desactivada");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login rechazado. Password incorrecta userId={}", user.getId());
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRol().name()
        );

        log.info("Login exitoso userId={}, email={}", user.getId(), user.getEmail());

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .userId(user.getId())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol().name())
                .expira(System.currentTimeMillis() + jwtService.getExpiration())
                .build();
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