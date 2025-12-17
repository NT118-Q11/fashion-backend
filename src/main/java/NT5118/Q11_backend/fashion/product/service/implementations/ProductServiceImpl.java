package NT5118.Q11_backend.fashion.product.service.implementations;

import NT5118.Q11_backend.fashion.product.dto.ProductCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.Product;
import NT5118.Q11_backend.fashion.product.repository.ProductRepository;
import NT5118.Q11_backend.fashion.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(ProductCreateRequest request) {
        // Check if product with same name already exists
        if (productRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Product with name '" + request.getName() + "' already exists");
        }

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
    public Product getProductById(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(String id, ProductUpdateRequest request) {
        // Find existing product
        Product product = getProductById(id);

        // Update only provided fields
        if (request.getName() != null) {
            // Check if new name conflicts with existing product
            if (!request.getName().equals(product.getName()) && 
                productRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Product with name '" + request.getName() + "' already exists");
            }
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
        product.updateTimestamp();

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String id) {
        // Check if product exists
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> filterByGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByGender(gender);
    }

    @Override
    public List<Product> filterByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null) {
            minPrice = 0.0;
        }
        if (maxPrice == null) {
            maxPrice = Double.MAX_VALUE;
        }
        
        if (minPrice < 0) {
            throw new IllegalArgumentException("Minimum price cannot be negative");
        }
        
        if (maxPrice < minPrice) {
            throw new IllegalArgumentException("Maximum price cannot be less than minimum price");
        }
        
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
