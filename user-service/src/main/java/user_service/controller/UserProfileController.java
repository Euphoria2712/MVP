package user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.domain.UserProfile;
import user_service.service.UserProfileService;
import jakarta.validation.Valid;
import user_service.dto.UserProfileRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService profileService;

    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(profileService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> findById(@PathVariable String id) {
        return ResponseEntity.ok(profileService.findById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfile> findMyProfile(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(profileService.findByUserId(userId));
    }

    @PostMapping("/me")
    public ResponseEntity<UserProfile> createMyProfile(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody UserProfileRequest request) {
        return ResponseEntity.ok(profileService.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> update(
            @PathVariable String id,
            @Valid @RequestBody UserProfileRequest request) {
        return ResponseEntity.ok(profileService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}