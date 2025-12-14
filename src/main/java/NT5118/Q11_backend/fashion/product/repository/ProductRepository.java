package NT5118.Q11_backend.fashion.product.repository;

import NT5118.Q11_backend.fashion.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    // Search by name (case-insensitive, partial match)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Filter by gender
    List<Product> findByGender(String gender);
    
    // Filter by price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    
    // Filter by color
    List<Product> findByColor(String color);
    
    // Filter by size
    List<Product> findBySize(String size);
    
    // Check if product exists by name
    boolean existsByName(String name);
}
