package freshmart_service.controller;

import freshmart_service.dto.ErrorResponse;
import freshmart_service.dto.PriceResult;
import freshmart_service.dto.ProductRequest;
import freshmart_service.dto.ProductResponse;
import freshmart_service.dto.ValidationErrorResponse;
import freshmart_service.service.FreshmartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/freshmart")
@RequiredArgsConstructor
@Tag(name = "Freshmart API", description = "API para la gestión de productos y comparación de precios")
public class FreshmartController {

        private final FreshmartService freshmartService;

        @Operation(summary = "Obtener todos los productos")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/products")
        public ResponseEntity<List<ProductResponse>> getAllProducts() {
                return ResponseEntity.ok(freshmartService.findAllProducts());
        }

        @Operation(summary = "Obtener un producto por su ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/products/{id}")
        public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
                return ResponseEntity.ok(freshmartService.findProductById(id));
        }

        @Operation(summary = "Crear un nuevo producto")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/products")
        public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(freshmartService.createProduct(request));
        }

        @Operation(summary = "Actualizar un producto existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PutMapping("/products/{id}")
        public ResponseEntity<ProductResponse> updateProduct(
                        @PathVariable String id,
                        @Valid @RequestBody ProductRequest request) {
                return ResponseEntity.ok(freshmartService.updateProduct(id, request));
        }

        @Operation(summary = "Eliminar un producto existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @DeleteMapping("/products/{id}")
        public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
                freshmartService.deleteProduct(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Buscar precios de productos")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Precios encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceResult.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/prices")
        public ResponseEntity<List<PriceResult>> getPrices(@RequestParam String q) {
                return ResponseEntity.ok(freshmartService.searchPrices(q));
        }

        @Operation(summary = "Obtener precios de un producto por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Precios obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceResult.class))),
                        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/prices/{id}")
        public ResponseEntity<PriceResult> getPriceById(@PathVariable String id) {
                return ResponseEntity.ok(freshmartService.getPricesForProduct(id));
        }
}