package recipe_service.controller;

import lombok.RequiredArgsConstructor;

import recipe_service.service.RecipeService;
import recipe_service.dto.RecipeRequest;
import recipe_service.dto.RecipeResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    // GET /api/recipes
    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAll() {
        return ResponseEntity.ok(recipeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    // GET /api/recipes/search?q=pie
    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponse>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(recipeService.search(q));
    }

    // GET /api/recipes/category/postres
    @GetMapping("/category/{categoria}")
    public ResponseEntity<List<RecipeResponse>> getByCategory(
            @PathVariable String categoria) {
        return ResponseEntity.ok(recipeService.getByCategory(categoria));
    }

    // GET /api/recipes/tag/sin_horno
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<RecipeResponse>> getByTag(
            @PathVariable String tag) {
        return ResponseEntity.ok(recipeService.getByTag(tag));
    }

    // POST /api/recipes
    @PostMapping
    public ResponseEntity<RecipeResponse> create(
            @Valid @RequestBody RecipeRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recipeService.create(request));
    }

    // PUT /api/recipes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> update(
            @PathVariable String id,
            @Valid @RequestBody RecipeRequest request) {
        return ResponseEntity.ok(recipeService.update(id, request));
    }

    // DELETE /api/recipes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}