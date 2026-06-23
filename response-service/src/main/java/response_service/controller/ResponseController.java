package response_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response_service.dto.ChatRequest;
import response_service.dto.ErrorResponse;
import response_service.dto.FinalResponse;
import response_service.dto.ValidationErrorResponse;
import response_service.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/response")
@RequiredArgsConstructor
@Tag(name = "Response", description = "Generación de respuesta final para el usuario")
public class ResponseController {

    private final ResponseService responseService;

    @Operation(
            summary = "Procesar respuesta final",
            description = "Recibe el mensaje del usuario y genera una respuesta final combinando receta, precios y sucursales cuando corresponda"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Respuesta generada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinalResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o error de negocio",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<FinalResponse> process(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody ChatRequest request) {

        return ResponseEntity.ok(
                responseService.process(userId, request)
        );
    }
}