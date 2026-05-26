package freshmart_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import freshmart_service.dto.PriceResult;
import freshmart_service.service.FreshmartService;

import java.util.List;

@RestController
@RequestMapping("/api/freshmart")
@RequiredArgsConstructor
public class FreshmartController {

    private final FreshmartService freshmartService;

    // buscar precios por nombre de producto
    @GetMapping("/prices")
    public ResponseEntity<List<PriceResult>> getPrices(
            @RequestParam String q) {
        return ResponseEntity.ok(freshmartService.searchPrices(q));
    }

    // precio de un producto específico por ID
    @GetMapping("/prices/{id}")
    public ResponseEntity<PriceResult> getPriceById(
            @PathVariable String id) {
        PriceResult result = freshmartService.getPricesForProduct(id);
        return result != null
            ? ResponseEntity.ok(result)
            : ResponseEntity.notFound().build();
    }

    // cargar catálogo de prueba
    @PostMapping("/seed")
    public ResponseEntity<String> seed() {
        freshmartService.cargarProductosPrueba();
        return ResponseEntity.ok("Catálogo cargado correctamente");
    }
}