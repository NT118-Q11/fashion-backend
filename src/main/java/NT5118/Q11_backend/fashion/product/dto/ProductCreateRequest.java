package NT5118.Q11_backend.fashion.product.dto;

import jakarta.validation.constraints.*;

public class ProductCreateRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 200, message = "Product name must be between 1 and 200 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Thumbnail URL is required")
    private String thumbnail;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Unisex)$", message = "Gender must be Male, Female, or Unisex")
    private String gender;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Size is required")
    private String size;

    // Constructors
    public ProductCreateRequest() {}

    public ProductCreateRequest(String name, Double price, String thumbnail, String gender,
                                String description, String color, String size) {
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
        this.gender = gender;
        this.description = description;
        this.color = color;
        this.size = size;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
