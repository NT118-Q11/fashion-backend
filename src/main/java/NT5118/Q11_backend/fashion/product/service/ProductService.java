package NT5118.Q11_backend.fashion.product.service;

import NT5118.Q11_backend.fashion.product.dto.ProductCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    /**
     * Create a new product
     * @param request Product creation request
     * @return Created product
     */
    Product createProduct(ProductCreateRequest request);
    
    /**
     * Update an existing product
     * @param id Product ID
     * @param request Product update request
     * @return Updated product
     */
    Product updateProduct(String id, ProductUpdateRequest request);
    
    /**
     * Delete a product
     * @param id Product ID
     */
    void deleteProduct(String id);
    
    /**
     * Get product by ID
     * @param id Product ID
     * @return Product if found
     */
    Optional<Product> getProductById(String id);
    
    /**
     * Get all products
     * @return List of all products
     */
    List<Product> getAllProducts();
    
    /**
     * Search products by name
     * @param keyword Search keyword
     * @return List of matching products
     */
    List<Product> searchProducts(String keyword);
    
    /**
     * Filter products by criteria
     * @param gender Gender filter (optional)
     * @param minPrice Minimum price (optional)
     * @param maxPrice Maximum price (optional)
     * @return List of filtered products
     */
    List<Product> filterProducts(String gender, Double minPrice, Double maxPrice);
}
