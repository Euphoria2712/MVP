package intent_service.controller;

import intent_service.dto.ErrorResponse;
import intent_service.dto.IntentRequest;
import intent_service.dto.IntentResponse;
import intent_service.dto.ValidationErrorResponse;
import intent_service.service.IntentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intent")
@RequiredArgsConstructor
@Tag(name = "Intent", description = "Procesamiento de intención del usuario")
public class IntentController {

    private final IntentService intentService;

    @Operation(
            summary = "Procesar intención",
            description = "Analiza el mensaje del usuario y determina si corresponde a receta, búsqueda, chat u otra acción"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Intención procesada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IntentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o error de negocio",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<IntentResponse> process(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody IntentRequest request) {

        return ResponseEntity.ok(
                intentService.process(request, userId)
        );
    }
}