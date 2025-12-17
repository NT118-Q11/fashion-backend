package NT5118.Q11_backend.fashion.product.controller;

import NT5118.Q11_backend.fashion.product.dto.ProductCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductResponse;
import NT5118.Q11_backend.fashion.product.dto.ProductUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.Product;
import NT5118.Q11_backend.fashion.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Create a new product
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product product = productService.createProduct(request);
        ProductResponse response = ProductResponse.fromProduct(product);
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Product created successfully");
        responseBody.put("product", response);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    /**
     * Get all products
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> responses = products.stream()
            .map(ProductResponse::fromProduct)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Get product by ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        ProductResponse response = ProductResponse.fromProduct(product);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Update product
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductUpdateRequest request) {
        Product product = productService.updateProduct(id, request);
        ProductResponse response = ProductResponse.fromProduct(product);
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Product updated successfully");
        responseBody.put("product", response);
        
        return ResponseEntity.ok(responseBody);
    }

    /**
     * Delete product
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Product deleted successfully");
        
        return ResponseEntity.ok(responseBody);
    }

    /**
     * Search products by keyword
     * GET /api/products/search?keyword={keyword}
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        List<ProductResponse> responses = products.stream()
            .map(ProductResponse::fromProduct)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Filter products by gender
     * GET /api/products/filter/gender?value={gender}
     */
    @GetMapping("/filter/gender")
    public ResponseEntity<List<ProductResponse>> filterByGender(
            @RequestParam(required = false) String value) {
        List<Product> products = productService.filterByGender(value);
        List<ProductResponse> responses = products.stream()
            .map(ProductResponse::fromProduct)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Filter products by price range
     * GET /api/products/filter/price?min={minPrice}&max={maxPrice}
     */
    @GetMapping("/filter/price")
    public ResponseEntity<List<ProductResponse>> filterByPriceRange(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) {
        List<Product> products = productService.filterByPriceRange(min, max);
        List<ProductResponse> responses = products.stream()
            .map(ProductResponse::fromProduct)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
}
