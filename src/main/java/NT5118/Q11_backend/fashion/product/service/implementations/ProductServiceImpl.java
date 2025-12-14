package NT5118.Q11_backend.fashion.product.service.implementations;

import NT5118.Q11_backend.fashion.product.dto.ProductCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.Product;
import NT5118.Q11_backend.fashion.product.repository.ProductRepository;
import NT5118.Q11_backend.fashion.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        // Create new product
        Product product = new Product(
            request.getName(),
            request.getPrice(),
            request.getThumbnail(),
            request.getGender(),
            request.getDescription(),
            request.getColor(),
            request.getSize()
        );
        
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public Product updateProduct(String id, ProductUpdateRequest request) {
        // Find existing product
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        
        // Update fields if provided
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getThumbnail() != null) {
            product.setThumbnail(request.getThumbnail());
        }
        if (request.getGender() != null) {
            product.setGender(request.getGender());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getColor() != null) {
            product.setColor(request.getColor());
        }
        if (request.getSize() != null) {
            product.setSize(request.getSize());
        }
        
        // Update timestamp
        product.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public void deleteProduct(String id) {
        // Check if product exists
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        
        productRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> filterProducts(String gender, Double minPrice, Double maxPrice) {
        // If all filters are null, return all products
        if (gender == null && minPrice == null && maxPrice == null) {
            return productRepository.findAll();
        }
        
        // Set default values for price range if not provided
        Double min = (minPrice != null) ? minPrice : 0.0;
        Double max = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;
        
        // Filter by gender and price range
        if (gender != null && !gender.trim().isEmpty()) {
            return productRepository.findByGenderAndPriceBetween(gender, min, max);
        }
        
        // Filter by price range only
        return productRepository.findByPriceBetween(min, max);
    }
}
