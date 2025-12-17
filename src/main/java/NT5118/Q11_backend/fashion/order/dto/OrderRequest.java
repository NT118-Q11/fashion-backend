package NT5118.Q11_backend.fashion.order.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private BigDecimal totalPrice;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    // Constructors
    public OrderRequest() {
    }

    public OrderRequest(String userId, BigDecimal totalPrice, String status, String shippingAddress) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }

    // Getters and Setters
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
}
