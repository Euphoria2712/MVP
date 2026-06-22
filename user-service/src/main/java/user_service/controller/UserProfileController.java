package user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.domain.UserProfile;
import user_service.service.UserProfileService;
import jakarta.validation.Valid;
import user_service.dto.UserProfileRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import user_service.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/users/profiles")
@RequiredArgsConstructor
@Tag(name = "Perfiles de Usuario", description = "Gestión de perfiles de usuario")
public class UserProfileController {

        private final UserProfileService profileService;

        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Perfiles obtenidos exitosamente"),
                        @ApiResponse(responseCode = "404", description = "No se encontraron perfiles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @Operation(summary = "Obtener todos los perfiles de usuario", description = "Retorna la lista completa de perfiles de usuario registrados")
        @GetMapping
        public ResponseEntity<List<UserProfile>> findAll() {
                return ResponseEntity.ok(profileService.findAll());
        }

        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Perfil no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @Operation(summary = "Buscar perfil por ID", description = "Obtiene la información de un perfil específico")
        @GetMapping("/{id}")
        public ResponseEntity<UserProfile> findById(@PathVariable String id) {
                return ResponseEntity.ok(profileService.findById(id));
        }

        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Perfil no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @Operation(summary = "Obtener perfil del usuario", description = "Retorna el perfil del usuario autenticado")
        @GetMapping("/me")
        public ResponseEntity<UserProfile> findMyProfile(
                        @RequestHeader("X-User-Id") String userId) {
                return ResponseEntity.ok(profileService.findByUserId(userId));
        }

        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Perfil creado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @Operation(summary = "Crear perfil para el usuario", description = "Crea un nuevo perfil asociado al usuario autenticado")
        @PostMapping("/me")
        public ResponseEntity<UserProfile> createMyProfile(
                        @RequestHeader("X-User-Id") String userId,
                        @Valid @RequestBody UserProfileRequest request) {
                return ResponseEntity.ok(profileService.create(userId, request));
        }

        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Perfil no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @Operation(summary = "Actualizar perfil de usuario", description = "Actualiza los datos del perfil de un usuario existente")
        @PutMapping("/{id}")
        public ResponseEntity<UserProfile> update(
                        @PathVariable String id,
                        @Valid @RequestBody UserProfileRequest request) {
                return ResponseEntity.ok(profileService.update(id, request));
        }

        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Perfil eliminado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Perfil no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @Operation(summary = "Eliminar perfil de usuario", description = "Elimina un perfil de usuario del sistema")
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable String id) {
                profileService.delete(id);
                return ResponseEntity.noContent().build();
        }
}