package NT5118.Q11_backend.fashion.rating.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "ratings")
public class Rating {
    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String productId;

    private Integer rateStars; // 1-5 stars

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructors
    public Rating() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Rating(String userId, String productId, Integer rateStars, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.rateStars = rateStars;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

