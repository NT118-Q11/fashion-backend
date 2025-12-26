package NT5118.Q11_backend.fashion.product.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "product_information")
public class ProductInformation {

    @Id
    private String id;

    @Indexed(unique = true)
    private String productId;

    @Indexed
    private String brand;

    @Indexed
    private String category;

    @Indexed
    private String gender;

    private String fit;

    private String care;

    @Indexed
    private String origin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructors
    public ProductInformation() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ProductInformation(String productId, String brand, String category,
                               String gender, String fit, String care, String origin) {
        this.productId = productId;
        this.brand = brand;
        this.category = category;
        this.gender = gender;
        this.fit = fit;
        this.care = care;
        this.origin = origin;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Method to update timestamp
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
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

    @Override
    public String toString() {
        return "ProductInformation{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", gender='" + gender + '\'' +
                ", fit='" + fit + '\'' +
                ", care='" + care + '\'' +
                ", origin='" + origin + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

