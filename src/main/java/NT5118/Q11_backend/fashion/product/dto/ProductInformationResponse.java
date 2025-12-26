package NT5118.Q11_backend.fashion.product.dto;

import NT5118.Q11_backend.fashion.product.model.ProductInformation;
import java.time.LocalDateTime;

public class ProductInformationResponse {
    private String id;
    private String productId;
    private String brand;
    private String category;
    private String gender;
    private String fit;
    private String care;
    private String origin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ProductInformationResponse() {}

    public ProductInformationResponse(ProductInformation productInfo) {
        this.id = productInfo.getId();
        this.productId = productInfo.getProductId();
        this.brand = productInfo.getBrand();
        this.category = productInfo.getCategory();
        this.gender = productInfo.getGender();
        this.fit = productInfo.getFit();
        this.care = productInfo.getCare();
        this.origin = productInfo.getOrigin();
        this.createdAt = productInfo.getCreatedAt();
        this.updatedAt = productInfo.getUpdatedAt();
    }

    // Static factory method
    public static ProductInformationResponse fromProductInformation(ProductInformation productInfo) {
        return new ProductInformationResponse(productInfo);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getCare() {
        return care;
    }

    public void setCare(String care) {
        this.care = care;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

