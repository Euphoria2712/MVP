package response_service.controller;


import lombok.RequiredArgsConstructor;
import response_service.dto.ChatRequest;
import response_service.dto.FinalResponse;
import response_service.service.ResponseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/response")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<FinalResponse> process(
        @RequestHeader("X-User-Id") String userId,
        @RequestBody ChatRequest request) {

        return ResponseEntity.ok(
            responseService.process(userId, request)
        );
    }
}