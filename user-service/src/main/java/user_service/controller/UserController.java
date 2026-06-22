package user_service.controller;

import lombok.RequiredArgsConstructor;
import user_service.domain.UserMemory;
import user_service.dto.UpdateUserRequest;
import user_service.dto.UserMemoryRequest;
import user_service.dto.UserResponse;
import user_service.repository.UserMemoryRepository;
import user_service.service.UserMemoryService;
import user_service.service.UserService;
import jakarta.validation.Valid;
import user_service.dto.UserMemoryRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import user_service.dto.ErrorResponse;
import user_service.dto.ValidationErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios y memorias")
public class UserController {

    private final UserMemoryService memoryService;
    private final UserService userService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            
    })
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna la lista completa de usuarios registrados")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene la información de un usuario específico")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Obtener información del usuario autenticado", description = "Retorna los datos del usuario actualmente autenticado")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Memorias obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Obtener memorias del usuario", description = "Retorna la lista de memorias asociadas al usuario")
    @GetMapping("/me/memories")
    public ResponseEntity<List<UserMemory>> getMemories(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(memoryService.findByUser(userId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Memoria guardada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Guardar memoria del usuario", description = "Crea una nueva memoria asociada al usuario")
    @PostMapping("/me/memories")
    public ResponseEntity<UserMemory> saveMemory(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody UserMemoryRequest memory) {
        return ResponseEntity.ok(memoryService.create(userId, memory));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Memoria obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Memoria no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Buscar memoria por ID", description = "Obtiene la información de una memoria específica")
    @GetMapping("/memories/{id}")
    public ResponseEntity<UserMemory> findMemoryById(@PathVariable String id) {
        return ResponseEntity.ok(memoryService.findById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Memoria actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Memoria no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Actualizar memoria", description = "Actualiza los datos de una memoria existente")
    @PutMapping("/memories/{id}")
    public ResponseEntity<UserMemory> updateMemory(
            @PathVariable String id,
            @Valid @RequestBody UserMemoryRequest memory) {
        return ResponseEntity.ok(memoryService.update(id, memory));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Memoria eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Memoria no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Eliminar memoria", description = "Elimina una memoria del sistema")
    @DeleteMapping("/memories/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable String id) {
        memoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}