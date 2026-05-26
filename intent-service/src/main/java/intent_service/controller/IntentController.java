package intent_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import intent_service.dto.IntentRequest;
import intent_service.dto.IntentResponse;
import intent_service.service.IntentService;

@RestController
@RequestMapping("/api/intent")
@RequiredArgsConstructor
public class IntentController {

    private final IntentService intentService;

    @PostMapping
    public ResponseEntity<IntentResponse> process(
        @RequestHeader("X-User-Id") String userId,
        @RequestBody IntentRequest request) {

        return ResponseEntity.ok(
            intentService.process(request, userId)
        );
    }
}