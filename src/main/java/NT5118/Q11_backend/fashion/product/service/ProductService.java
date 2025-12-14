package NT5118.Q11_backend.fashion.product.service;

import NT5118.Q11_backend.fashion.product.dto.ProductCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.Product;

import java.util.List;

public interface ProductService {
    
    /**
     * Create a new product
     * @param request Product creation request
     * @return Created product
     */
    Product createProduct(ProductCreateRequest request);
    
    /**
     * Get product by ID
     * @param id Product ID
     * @return Product
     */
    Product getProductById(String id);
    
    /**
     * Get all products
     * @return List of all products
     */
    List<Product> getAllProducts();
    
    /**
     * Update product
     * @param id Product ID
     * @param request Product update request
     * @return Updated product
     */
    Product updateProduct(String id, ProductUpdateRequest request);
    
    /**
     * Delete product
     * @param id Product ID
     */
    void deleteProduct(String id);
    
    /**
     * Search products by keyword (searches in name)
     * @param keyword Search keyword
     * @return List of matching products
     */
    List<Product> searchProducts(String keyword);
    
    /**
     * Filter products by gender
     * @param gender Gender filter
     * @return List of filtered products
     */
    List<Product> filterByGender(String gender);
    
    /**
     * Filter products by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of filtered products
     */
    List<Product> filterByPriceRange(Double minPrice, Double maxPrice);
}
