package NT5118.Q11_backend.fashion.rating.service.implementations;

import NT5118.Q11_backend.fashion.rating.dto.RatingRequest;
import NT5118.Q11_backend.fashion.rating.dto.RatingResponse;
import NT5118.Q11_backend.fashion.rating.model.Rating;
import NT5118.Q11_backend.fashion.rating.repository.RatingRepository;
import NT5118.Q11_backend.fashion.rating.service.RatingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public RatingResponse createRating(RatingRequest request) {
        Rating rating = new Rating();
        rating.setUserId(request.getUserId());
        rating.setProductId(request.getProductId());
        rating.setRateStars(request.getRateStars());
        rating.setComment(request.getComment());
        rating.setCreatedAt(LocalDateTime.now());
        rating.setUpdatedAt(LocalDateTime.now());

        Rating savedRating = ratingRepository.save(rating);
        return convertToResponse(savedRating);
    }

    @Override
    public Optional<RatingResponse> getRatingById(String id) {
        return ratingRepository.findById(id)
                .map(this::convertToResponse);
    }

    @Override
    public List<RatingResponse> getAllRatings() {
        return ratingRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getRatingsByProductId(String productId) {
        return ratingRepository.findByProductId(productId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getRatingsByUserId(String userId) {
        return ratingRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RatingResponse updateRating(String id, RatingRequest request) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + id));

        rating.setRateStars(request.getRateStars());
        rating.setComment(request.getComment());
        rating.setUpdatedAt(LocalDateTime.now());

        Rating updatedRating = ratingRepository.save(rating);
        return convertToResponse(updatedRating);
    }

    @Override
    public void deleteRating(String id) {
        ratingRepository.deleteById(id);
    }

    private RatingResponse convertToResponse(Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getUserId(),
                rating.getProductId(),
                rating.getRateStars(),
                rating.getComment(),
                rating.getCreatedAt(),
                rating.getUpdatedAt()
        );
    }
}

