package NT5118.Q11_backend.fashion.cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CartResponse {
    private String id;
    private String userId;
    private List<CartItemResponse> items;
    private Double totalPrice;
    private Integer totalItems;
    private LocalDateTime updatedAt;

    // Constructors
    public CartResponse() {}

    public CartResponse(String id, String userId, List<CartItemResponse> items, 
                       Double totalPrice, Integer totalItems, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.updatedAt = updatedAt;
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

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
