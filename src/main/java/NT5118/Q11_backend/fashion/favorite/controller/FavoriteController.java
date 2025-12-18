package NT5118.Q11_backend.fashion.favorite.controller;

import NT5118.Q11_backend.fashion.favorite.dto.FavoriteRequest;
import NT5118.Q11_backend.fashion.favorite.dto.FavoriteResponse;
import NT5118.Q11_backend.fashion.favorite.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * Add product to favorites
     * POST /api/favorites
     */
    @PostMapping
    public ResponseEntity<?> addFavorite(@Valid @RequestBody FavoriteRequest request) {
        try {
            // Note: Ideally userId should come from SecurityContext if authenticated
            FavoriteResponse response = favoriteService.addFavorite(request.getUserId(), request.getProductId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Remove product from favorites
     * DELETE /api/favorites?userId={userId}&productId={productId}
     */
    @DeleteMapping
    public ResponseEntity<?> removeFavorite(
            @RequestParam String userId,
            @RequestParam String productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok(Map.of("message", "Removed from favorites"));
    }

    /**
     * Get user's favorites
     * GET /api/favorites?userId={userId}
     */
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@RequestParam String userId) {
        List<FavoriteResponse> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * Check if product is favorited
     * GET /api/favorites/check?userId={userId}&productId={productId}
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @RequestParam String userId,
            @RequestParam String productId) {
        boolean isFavorite = favoriteService.isFavorite(userId, productId);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
}
