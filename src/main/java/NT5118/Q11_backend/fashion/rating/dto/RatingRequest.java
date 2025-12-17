package NT5118.Q11_backend.fashion.rating.dto;

import jakarta.validation.constraints.*;

public class RatingRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotNull(message = "Rate stars is required")
    @Min(value = 1, message = "Rating must be at least 1 star")
    @Max(value = 5, message = "Rating must be at most 5 stars")
    private Integer rateStars;

    private String comment;

    // Constructors
    public RatingRequest() {}

    public RatingRequest(String userId, String productId, Integer rateStars, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.rateStars = rateStars;
        this.comment = comment;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getRateStars() {
        return rateStars;
    }

    public void setRateStars(Integer rateStars) {
        this.rateStars = rateStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

