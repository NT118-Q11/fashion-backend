package NT5118.Q11_backend.fashion.rating.service;

import NT5118.Q11_backend.fashion.rating.dto.RatingRequest;
import NT5118.Q11_backend.fashion.rating.dto.RatingResponse;
import java.util.List;
import java.util.Optional;

public interface RatingService {
    RatingResponse createRating(RatingRequest request);
    Optional<RatingResponse> getRatingById(String id);
    List<RatingResponse> getAllRatings();
    List<RatingResponse> getRatingsByProductId(String productId);
    List<RatingResponse> getRatingsByUserId(String userId);
    RatingResponse updateRating(String id, RatingRequest request);
    void deleteRating(String id);
}

