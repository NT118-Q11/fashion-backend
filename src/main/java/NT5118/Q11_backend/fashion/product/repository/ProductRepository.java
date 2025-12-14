package NT5118.Q11_backend.fashion.product.repository;

import NT5118.Q11_backend.fashion.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    // Find products by gender
    List<Product> findByGender(String gender);
    
    // Search products by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Filter products by price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    
    // Find by gender and price range
    List<Product> findByGenderAndPriceBetween(String gender, Double minPrice, Double maxPrice);
}
