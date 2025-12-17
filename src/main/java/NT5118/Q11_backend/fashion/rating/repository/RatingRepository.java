package NT5118.Q11_backend.fashion.rating.repository;

import NT5118.Q11_backend.fashion.rating.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByProductId(String productId);
    List<Rating> findByUserId(String userId);
    List<Rating> findByProductIdAndUserId(String productId, String userId);
}

