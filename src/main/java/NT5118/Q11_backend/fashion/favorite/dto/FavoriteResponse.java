package NT5118.Q11_backend.fashion.favorite.dto;

import NT5118.Q11_backend.fashion.favorite.model.Favorite;
import NT5118.Q11_backend.fashion.product.model.Product; // Assuming we might want to return full product details

import java.time.LocalDateTime;

public class FavoriteResponse {
    private String id;
    private String userId;
    private String productId;
    private LocalDateTime createdAt;
    
    // Optional: Include full product details if available
    private Product product;

    public FavoriteResponse() {}

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.userId = favorite.getUserId();
        this.productId = favorite.getProductId();
        this.createdAt = favorite.getCreatedAt();
    }
    
    public static FavoriteResponse fromFavorite(Favorite favorite) {
        return new FavoriteResponse(favorite);
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
