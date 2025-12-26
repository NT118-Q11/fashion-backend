package NT5118.Q11_backend.fashion.product.service;

import NT5118.Q11_backend.fashion.product.dto.ProductInformationCreateRequest;
import NT5118.Q11_backend.fashion.product.dto.ProductInformationUpdateRequest;
import NT5118.Q11_backend.fashion.product.model.ProductInformation;

import java.util.List;

public interface ProductInformationService {

    /**
     * Create product information
     * @param request Product information creation request
     * @return Created product information
     */
    ProductInformation createProductInformation(ProductInformationCreateRequest request);

    /**
     * Get product information by ID
     * @param id Product information ID
     * @return Product information
     */
    ProductInformation getProductInformationById(String id);

    /**
     * Get product information by product ID
     * @param productId Product ID
     * @return Product information
     */
    ProductInformation getProductInformationByProductId(String productId);

    /**
     * Get all product information
     * @return List of all product information
     */
    List<ProductInformation> getAllProductInformation();

    /**
     * Update product information
     * @param id Product information ID
     * @param request Product information update request
     * @return Updated product information
     */
    ProductInformation updateProductInformation(String id, ProductInformationUpdateRequest request);

    /**
     * Update product information by product ID
     * @param productId Product ID
     * @param request Product information update request
     * @return Updated product information
     */
    ProductInformation updateProductInformationByProductId(String productId, ProductInformationUpdateRequest request);

    /**
     * Delete product information
     * @param id Product information ID
     */
    void deleteProductInformation(String id);

    /**
     * Delete product information by product ID
     * @param productId Product ID
     */
    void deleteProductInformationByProductId(String productId);

    /**
     * Filter product information by brand
     * @param brand Brand name
     * @return List of product information
     */
    List<ProductInformation> filterByBrand(String brand);

    /**
     * Filter product information by category
     * @param category Category name
     * @return List of product information
     */
    List<ProductInformation> filterByCategory(String category);

    /**
     * Filter product information by gender
     * @param gender Gender
     * @return List of product information
     */
    List<ProductInformation> filterByGender(String gender);

    /**
     * Filter product information by origin
     * @param origin Country of origin
     * @return List of product information
     */
    List<ProductInformation> filterByOrigin(String origin);

    /**
     * Filter product information by fit
     * @param fit Fit type
     * @return List of product information
     */
    List<ProductInformation> filterByFit(String fit);
}

