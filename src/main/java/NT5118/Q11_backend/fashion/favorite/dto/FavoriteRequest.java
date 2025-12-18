package NT5118.Q11_backend.fashion.favorite.dto;

import jakarta.validation.constraints.NotBlank;

public class FavoriteRequest {
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    // User ID allows explicit user setting, 
    // though typically this comes from the authenticated user context or query param
    private String userId;

    public FavoriteRequest() {}

    public FavoriteRequest(String productId, String userId) {
        this.productId = productId;
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
