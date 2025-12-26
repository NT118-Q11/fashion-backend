package NT5118.Q11_backend.fashion.product.controller;

import NT5118.Q11_backend.fashion.product.dto.ProductInformationCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductInformationResponse;
import NT5118.Q11_backend.fashion.product.dto.ProductInformationUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.ProductInformation;
import NT5118.Q11_backend.fashion.product.service.ProductInformationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product-information")
public class ProductInformationController {

    private final ProductInformationService productInformationService;

    public ProductInformationController(ProductInformationService productInformationService) {
        this.productInformationService = productInformationService;
    }

    /**
     * Create product information
     * POST /api/product-information
     */
    @PostMapping
    public ResponseEntity<?> createProductInformation(@Valid @RequestBody ProductInformationCreateRequest request) {
        ProductInformation productInfo = productInformationService.createProductInformation(request);
        ProductInformationResponse response = ProductInformationResponse.fromProductInformation(productInfo);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Product information created successfully");
        responseBody.put("productInformation", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    /**
     * Get all product information
     * GET /api/product-information
     */
    @GetMapping
    public ResponseEntity<List<ProductInformationResponse>> getAllProductInformation() {
        List<ProductInformation> productInfoList = productInformationService.getAllProductInformation();
        List<ProductInformationResponse> responses = productInfoList.stream()
            .map(ProductInformationResponse::fromProductInformation)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Get product information by ID
     * GET /api/product-information/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductInformationResponse> getProductInformationById(@PathVariable String id) {
        ProductInformation productInfo = productInformationService.getProductInformationById(id);
        ProductInformationResponse response = ProductInformationResponse.fromProductInformation(productInfo);

        return ResponseEntity.ok(response);
    }

    /**
     * Get product information by product ID
     * GET /api/product-information/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductInformationResponse> getProductInformationByProductId(@PathVariable String productId) {
        ProductInformation productInfo = productInformationService.getProductInformationByProductId(productId);
        ProductInformationResponse response = ProductInformationResponse.fromProductInformation(productInfo);

        return ResponseEntity.ok(response);
    }

    /**
     * Update product information by ID
     * PUT /api/product-information/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductInformation(
            @PathVariable String id,
            @Valid @RequestBody ProductInformationUpdateRequest request) {
        ProductInformation productInfo = productInformationService.updateProductInformation(id, request);
        ProductInformationResponse response = ProductInformationResponse.fromProductInformation(productInfo);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Product information updated successfully");
        responseBody.put("productInformation", response);

        return ResponseEntity.ok(responseBody);
    }

    /**
     * Update product information by product ID
     * PUT /api/product-information/product/{productId}
     */
    @PutMapping("/product/{productId}")
    public ResponseEntity<?> updateProductInformationByProductId(
            @PathVariable String productId,
            @Valid @RequestBody ProductInformationUpdateRequest request) {
        ProductInformation productInfo = productInformationService.updateProductInformationByProductId(productId, request);
        ProductInformationResponse response = ProductInformationResponse.fromProductInformation(productInfo);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Product information updated successfully");
        responseBody.put("productInformation", response);

        return ResponseEntity.ok(responseBody);
    }

    /**
     * Delete product information by ID
     * DELETE /api/product-information/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductInformation(@PathVariable String id) {
        productInformationService.deleteProductInformation(id);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Product information deleted successfully");

        return ResponseEntity.ok(responseBody);
    }

    /**
     * Delete product information by product ID
     * DELETE /api/product-information/product/{productId}
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteProductInformationByProductId(@PathVariable String productId) {
        productInformationService.deleteProductInformationByProductId(productId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Product information deleted successfully");

        return ResponseEntity.ok(responseBody);
    }

    /**
     * Filter product information by brand
     * GET /api/product-information/filter/brand?value={brand}
     */
    @GetMapping("/filter/brand")
    public ResponseEntity<List<ProductInformationResponse>> filterByBrand(
            @RequestParam(required = false) String value) {
        List<ProductInformation> productInfoList = productInformationService.filterByBrand(value);
        List<ProductInformationResponse> responses = productInfoList.stream()
            .map(ProductInformationResponse::fromProductInformation)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Filter product information by category
     * GET /api/product-information/filter/category?value={category}
     */
    @GetMapping("/filter/category")
    public ResponseEntity<List<ProductInformationResponse>> filterByCategory(
            @RequestParam(required = false) String value) {
        List<ProductInformation> productInfoList = productInformationService.filterByCategory(value);
        List<ProductInformationResponse> responses = productInfoList.stream()
            .map(ProductInformationResponse::fromProductInformation)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Filter product information by gender
     * GET /api/product-information/filter/gender?value={gender}
     */
    @GetMapping("/filter/gender")
    public ResponseEntity<List<ProductInformationResponse>> filterByGender(
            @RequestParam(required = false) String value) {
        List<ProductInformation> productInfoList = productInformationService.filterByGender(value);
        List<ProductInformationResponse> responses = productInfoList.stream()
            .map(ProductInformationResponse::fromProductInformation)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Filter product information by origin
     * GET /api/product-information/filter/origin?value={origin}
     */
    @GetMapping("/filter/origin")
    public ResponseEntity<List<ProductInformationResponse>> filterByOrigin(
            @RequestParam(required = false) String value) {
        List<ProductInformation> productInfoList = productInformationService.filterByOrigin(value);
        List<ProductInformationResponse> responses = productInfoList.stream()
            .map(ProductInformationResponse::fromProductInformation)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Filter product information by fit
     * GET /api/product-information/filter/fit?value={fit}
     */
    @GetMapping("/filter/fit")
    public ResponseEntity<List<ProductInformationResponse>> filterByFit(
            @RequestParam(required = false) String value) {
        List<ProductInformation> productInfoList = productInformationService.filterByFit(value);
        List<ProductInformationResponse> responses = productInfoList.stream()
            .map(ProductInformationResponse::fromProductInformation)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}

