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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMemoryService memoryService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    // el userId viene del header que pone el Gateway
    @GetMapping("/me/memories")
    public ResponseEntity<List<UserMemory>> getMemories(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(memoryService.findByUser(userId));
    }

    @PostMapping("/me/memories")
    public ResponseEntity<UserMemory> saveMemory(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody UserMemoryRequest memory) {
        return ResponseEntity.ok(memoryService.create(userId, memory));
    }

    @GetMapping("/memories/{id}")
    public ResponseEntity<UserMemory> findMemoryById(@PathVariable String id) {
        return ResponseEntity.ok(memoryService.findById(id));
    }

    @PutMapping("/memories/{id}")
    public ResponseEntity<UserMemory> updateMemory(
            @PathVariable String id,
            @Valid @RequestBody UserMemoryRequest memory) {
        return ResponseEntity.ok(memoryService.update(id, memory));
    }

    @DeleteMapping("/memories/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable String id) {
        memoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}