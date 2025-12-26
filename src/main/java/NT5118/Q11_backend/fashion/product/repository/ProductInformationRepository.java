package NT5118.Q11_backend.fashion.product.repository;

import NT5118.Q11_backend.fashion.product.model.ProductInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInformationRepository extends MongoRepository<ProductInformation, String> {

    // Find by productId
    Optional<ProductInformation> findByProductId(String productId);

    // Check if product information exists by productId
    boolean existsByProductId(String productId);

    // Delete by productId
    void deleteByProductId(String productId);

    // Filter by brand
    List<ProductInformation> findByBrand(String brand);

    // Filter by brand (case-insensitive)
    List<ProductInformation> findByBrandIgnoreCase(String brand);

    // Filter by category
    List<ProductInformation> findByCategory(String category);

    // Filter by category (case-insensitive)
    List<ProductInformation> findByCategoryIgnoreCase(String category);

    // Filter by gender
    List<ProductInformation> findByGender(String gender);

    // Filter by fit
    List<ProductInformation> findByFit(String fit);

    // Filter by origin
    List<ProductInformation> findByOrigin(String origin);

    // Filter by origin (case-insensitive)
    List<ProductInformation> findByOriginIgnoreCase(String origin);

    // Filter by brand and category
    List<ProductInformation> findByBrandAndCategory(String brand, String category);

    // Filter by brand and gender
    List<ProductInformation> findByBrandAndGender(String brand, String gender);
}

