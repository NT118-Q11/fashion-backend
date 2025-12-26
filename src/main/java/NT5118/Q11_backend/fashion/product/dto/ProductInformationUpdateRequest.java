package NT5118.Q11_backend.fashion.product.dto;

import jakarta.validation.constraints.*;

public class ProductInformationUpdateRequest {

    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Pattern(regexp = "^(Male|Female|Unisex)$", message = "Gender must be Male, Female, or Unisex")
    private String gender;

    @Size(max = 50, message = "Fit must not exceed 50 characters")
    private String fit;

    @Size(max = 500, message = "Care instructions must not exceed 500 characters")
    private String care;

    @Size(max = 100, message = "Origin must not exceed 100 characters")
    private String origin;

    // Constructors
    public ProductInformationUpdateRequest() {}

    // Getters and Setters
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
}

