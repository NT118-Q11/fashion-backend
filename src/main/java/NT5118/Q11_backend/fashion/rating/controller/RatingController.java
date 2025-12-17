package NT5118.Q11_backend.fashion.rating.controller;

import NT5118.Q11_backend.fashion.rating.dto.RatingRequest;
import NT5118.Q11_backend.fashion.rating.dto.RatingResponse;
import NT5118.Q11_backend.fashion.rating.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Create a new rating
     * POST /api/ratings
     */
    @PostMapping
    public ResponseEntity<RatingResponse> createRating(@Valid @RequestBody RatingRequest request) {
        RatingResponse response = ratingService.createRating(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all ratings
     * GET /api/ratings
     */
    @GetMapping
    public ResponseEntity<List<RatingResponse>> getAllRatings() {
        List<RatingResponse> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    /**
     * Get rating by ID
     * GET /api/ratings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getRatingById(@PathVariable String id) {
        return ratingService.getRatingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all ratings for a specific product
     * GET /api/ratings/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByProductId(@PathVariable String productId) {
        List<RatingResponse> ratings = ratingService.getRatingsByProductId(productId);
        return ResponseEntity.ok(ratings);
    }

    /**
     * Get all ratings by a specific user
     * GET /api/ratings/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByUserId(@PathVariable String userId) {
        List<RatingResponse> ratings = ratingService.getRatingsByUserId(userId);
        return ResponseEntity.ok(ratings);
    }

    /**
     * Update a rating
     * PUT /api/ratings/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating(@PathVariable String id, @Valid @RequestBody RatingRequest request) {
        try {
            RatingResponse response = ratingService.updateRating(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a rating
     * DELETE /api/ratings/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable String id) {
        return ratingService.getRatingById(id)
                .map(rating -> {
                    ratingService.deleteRating(id);
                    return ResponseEntity.ok(Map.of("message", "Rating deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

