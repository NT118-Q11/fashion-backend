package NT5118.Q11_backend.fashion.cart.service;

import NT5118.Q11_backend.fashion.cart.dto.AddToCartRequest;
import NT5118.Q11_backend.fashion.cart.dto.CartResponse;
import NT5118.Q11_backend.fashion.cart.dto.UpdateCartItemRequest;

public interface CartService {
    
    /**
     * Add product to cart
     * @param request Add to cart request
     * @return Updated cart
     */
    CartResponse addToCart(AddToCartRequest request);
    
    /**
     * Get user's cart
     * @param userId User ID
     * @return User's cart
     */
    CartResponse getCart(String userId);
    
    /**
     * Update cart item quantity
     * @param userId User ID
     * @param cartItemId Cart item ID
     * @param request Update request
     * @return Updated cart
     */
    CartResponse updateCartItem(String userId, String cartItemId, UpdateCartItemRequest request);
    
    /**
     * Remove item from cart
     * @param userId User ID
     * @param cartItemId Cart item ID
     * @return Updated cart
     */
    CartResponse removeCartItem(String userId, String cartItemId);
    
    /**
     * Clear entire cart
     * @param userId User ID
     * @return Empty cart
     */
    CartResponse clearCart(String userId);
}
