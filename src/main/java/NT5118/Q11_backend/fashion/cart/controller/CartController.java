package NT5118.Q11_backend.fashion.cart.controller;

import NT5118.Q11_backend.fashion.cart.dto.AddToCartRequest;
import NT5118.Q11_backend.fashion.cart.dto.CartResponse;
import NT5118.Q11_backend.fashion.cart.dto.UpdateCartItemRequest;
import NT5118.Q11_backend.fashion.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add product to cart
     * POST /api/cart/items
     */
    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest request) {
        CartResponse cart = cartService.addToCart(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product added to cart successfully");
        response.put("cart", cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get user's cart
     * GET /api/cart?userId={userId}
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestParam String userId) {
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Update cart item quantity
     * PUT /api/cart/items/{id}?userId={userId}
     */
    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable String id,
            @RequestParam String userId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        CartResponse cart = cartService.updateCartItem(userId, id, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart item updated successfully");
        response.put("cart", cart);

        return ResponseEntity.ok(response);
    }

    /**
     * Remove item from cart
     * DELETE /api/cart/items/{id}?userId={userId}
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable String id,
            @RequestParam String userId) {
        CartResponse cart = cartService.removeCartItem(userId, id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item removed from cart successfully");
        response.put("cart", cart);

        return ResponseEntity.ok(response);
    }

    /**
     * Clear entire cart
     * DELETE /api/cart?userId={userId}
     */
    @DeleteMapping
    public ResponseEntity<?> clearCart(@RequestParam String userId) {
        CartResponse cart = cartService.clearCart(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        response.put("cart", cart);

        return ResponseEntity.ok(response);
    }
}
