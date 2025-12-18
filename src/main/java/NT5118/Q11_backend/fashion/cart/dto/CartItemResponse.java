package NT5118.Q11_backend.fashion.cart.dto;

import NT5118.Q11_backend.fashion.product.dto.ProductResponse;

public class CartItemResponse {
    private String id;
    private ProductResponse product;
    private Integer quantity;
    private Double subtotal;

    // Constructors
    public CartItemResponse() {}

    public CartItemResponse(String id, ProductResponse product, Integer quantity, Double subtotal) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
