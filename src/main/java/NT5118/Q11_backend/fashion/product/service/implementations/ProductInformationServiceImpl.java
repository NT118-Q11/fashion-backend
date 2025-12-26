package NT5118.Q11_backend.fashion.product.service.implementations;

import NT5118.Q11_backend.fashion.product.dto.ProductInformationCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductInformationUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.ProductInformation;
import NT5118.Q11_backend.fashion.product.repository.ProductInformationRepository;
import NT5118.Q11_backend.fashion.product.repository.ProductRepository;
import NT5118.Q11_backend.fashion.product.service.ProductInformationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInformationServiceImpl implements ProductInformationService {

    private final ProductInformationRepository productInformationRepository;
    private final ProductRepository productRepository;

    public ProductInformationServiceImpl(ProductInformationRepository productInformationRepository,
                                         ProductRepository productRepository) {
        this.productInformationRepository = productInformationRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductInformation createProductInformation(ProductInformationCreateRequest request) {
        // Validate that the product exists
        if (!productRepository.existsById(request.getProductId())) {
            throw new IllegalArgumentException("Product not found with id: " + request.getProductId());
        }

        // Check if product information already exists for this product
        if (productInformationRepository.existsByProductId(request.getProductId())) {
            throw new IllegalArgumentException("Product information already exists for product id: " + request.getProductId());
        }

        // Create new product information
        ProductInformation productInfo = new ProductInformation(
            request.getProductId(),
            request.getBrand(),
            request.getCategory(),
            request.getGender(),
            request.getFit(),
            request.getCare(),
            request.getOrigin()
        );

        return productInformationRepository.save(productInfo);
    }

    @Override
    public ProductInformation getProductInformationById(String id) {
        return productInformationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product information not found with id: " + id));
    }

    @Override
    public ProductInformation getProductInformationByProductId(String productId) {
        return productInformationRepository.findByProductId(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product information not found for product id: " + productId));
    }

    @Override
    public List<ProductInformation> getAllProductInformation() {
        return productInformationRepository.findAll();
    }

    @Override
    public ProductInformation updateProductInformation(String id, ProductInformationUpdateRequest request) {
        // Find existing product information
        ProductInformation productInfo = getProductInformationById(id);
        return updateProductInformationFields(productInfo, request);
    }

    @Override
    public ProductInformation updateProductInformationByProductId(String productId, ProductInformationUpdateRequest request) {
        // Find existing product information by product ID
        ProductInformation productInfo = getProductInformationByProductId(productId);
        return updateProductInformationFields(productInfo, request);
    }

    private ProductInformation updateProductInformationFields(ProductInformation productInfo, ProductInformationUpdateRequest request) {
        // Update only provided fields
        if (request.getBrand() != null) {
            productInfo.setBrand(request.getBrand());
        }

        if (request.getCategory() != null) {
            productInfo.setCategory(request.getCategory());
        }

        if (request.getGender() != null) {
            productInfo.setGender(request.getGender());
        }

        if (request.getFit() != null) {
            productInfo.setFit(request.getFit());
        }

        if (request.getCare() != null) {
            productInfo.setCare(request.getCare());
        }

        if (request.getOrigin() != null) {
            productInfo.setOrigin(request.getOrigin());
        }

        // Update timestamp
        productInfo.updateTimestamp();

        return productInformationRepository.save(productInfo);
    }

    @Override
    public void deleteProductInformation(String id) {
        // Check if product information exists
        if (!productInformationRepository.existsById(id)) {
            throw new IllegalArgumentException("Product information not found with id: " + id);
        }

        productInformationRepository.deleteById(id);
    }

    @Override
    public void deleteProductInformationByProductId(String productId) {
        // Check if product information exists for product ID
        if (!productInformationRepository.existsByProductId(productId)) {
            throw new IllegalArgumentException("Product information not found for product id: " + productId);
        }

        productInformationRepository.deleteByProductId(productId);
    }

    @Override
    public List<ProductInformation> filterByBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return getAllProductInformation();
        }
        return productInformationRepository.findByBrandIgnoreCase(brand);
    }

    @Override
    public List<ProductInformation> filterByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllProductInformation();
        }
        return productInformationRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<ProductInformation> filterByGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return getAllProductInformation();
        }
        return productInformationRepository.findByGender(gender);
    }

    @Override
    public List<ProductInformation> filterByOrigin(String origin) {
        if (origin == null || origin.trim().isEmpty()) {
            return getAllProductInformation();
        }
        return productInformationRepository.findByOriginIgnoreCase(origin);
    }

    @Override
    public List<ProductInformation> filterByFit(String fit) {
        if (fit == null || fit.trim().isEmpty()) {
            return getAllProductInformation();
        }
        return productInformationRepository.findByFit(fit);
    }
}

