package freshmart_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import freshmart_service.dto.PriceResult;
import freshmart_service.dto.ProductRequest;
import freshmart_service.dto.ProductResponse;
import freshmart_service.service.FreshmartService;

import java.util.List;

@RestController
@RequestMapping("/api/freshmart")
@RequiredArgsConstructor
public class FreshmartController {

    private final FreshmartService freshmartService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(freshmartService.findAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable String id) {
        return ResponseEntity.ok(freshmartService.findProductById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(freshmartService.createProduct(request));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(freshmartService.updateProduct(id, request));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        freshmartService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prices")
    public ResponseEntity<List<PriceResult>> getPrices(
            @RequestParam String q) {
        return ResponseEntity.ok(freshmartService.searchPrices(q));
    }

    @GetMapping("/prices/{id}")
    public ResponseEntity<PriceResult> getPriceById(
            @PathVariable String id) {
        return ResponseEntity.ok(freshmartService.getPricesForProduct(id));
    }
}