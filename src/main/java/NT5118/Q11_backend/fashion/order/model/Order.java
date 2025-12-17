package NT5118.Q11_backend.fashion.order.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed
    private String userId;

    private BigDecimal totalPrice;

    private String status; // e.g., "PENDING", "SHIPPING", "COMPLETED", "CANCELLED"

    private String shippingAddress;

    private LocalDateTime createdAt;

    // Constructors
    public Order() {
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public Order(String userId, BigDecimal totalPrice, String status, String shippingAddress) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.createdAt = LocalDateTime.now();
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
