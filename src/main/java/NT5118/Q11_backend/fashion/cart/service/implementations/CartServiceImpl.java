package NT5118.Q11_backend.fashion.cart.service.implementations;

import NT5118.Q11_backend.fashion.cart.dto.AddToCartRequest;
import NT5118.Q11_backend.fashion.cart.dto.CartItemResponse;
import NT5118.Q11_backend.fashion.cart.dto.CartResponse;
import NT5118.Q11_backend.fashion.cart.dto.UpdateCartItemRequest;
import NT5118.Q11_backend.fashion.cart.model.Cart;
import NT5118.Q11_backend.fashion.cart.model.CartItem;
import NT5118.Q11_backend.fashion.cart.repository.CartItemRepository;
import NT5118.Q11_backend.fashion.cart.repository.CartRepository;
import NT5118.Q11_backend.fashion.cart.service.CartService;
import NT5118.Q11_backend.fashion.product.dto.ProductResponse;
import NT5118.Q11_backend.fashion.product.model.Product;
import NT5118.Q11_backend.fashion.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                          CartItemRepository cartItemRepository,
                          ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        // Validate product exists
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + request.getProductId()));

        // Get or create cart for user
        Cart cart = getOrCreateCart(request.getUserId());

        // Check if product already in cart
        Optional<CartItem> existingItem = cartItemRepository
            .findByCartIdAndProductId(cart.getId(), request.getProductId());

        if (existingItem.isPresent()) {
            // Update quantity of existing item
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.updateTimestamp();
            cartItemRepository.save(item);
        } else {
            // Create new cart item
            CartItem newItem = new CartItem(cart.getId(), request.getProductId(), request.getQuantity(), request.getSelectedSize(), request.getSelectedColor());
            cartItemRepository.save(newItem);
        }

        // Recalculate total price
        recalculateTotalPrice(cart);

        // Return updated cart
        return buildCartResponse(cart);
    }

    @Override
    public CartResponse getCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(String userId, String cartItemId, UpdateCartItemRequest request) {
        // Find cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        // Verify ownership
        Cart cart = cartRepository.findById(cartItem.getCartId())
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (!cart.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this cart item");
        }

        // Update quantity or remove if 0
        if (request.getQuantity() == 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(request.getQuantity());
            cartItem.updateTimestamp();
            cartItemRepository.save(cartItem);
        }

        // Recalculate total price
        recalculateTotalPrice(cart);

        // Return updated cart
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeCartItem(String userId, String cartItemId) {
        // Find cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        // Verify ownership
        Cart cart = cartRepository.findById(cartItem.getCartId())
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (!cart.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to remove this cart item");
        }

        // Delete cart item
        cartItemRepository.delete(cartItem);

        // Recalculate total price
        recalculateTotalPrice(cart);

        // Return updated cart
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);

        // Delete all cart items
        cartItemRepository.deleteByCartId(cart.getId());

        // Reset total price
        cart.setTotalPrice(0.0);
        cart.updateTimestamp();
        cartRepository.save(cart);

        // Return empty cart
        return buildCartResponse(cart);
    }

    // Helper methods

    private Cart getOrCreateCart(String userId) {
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            // Create new cart for user
            Cart newCart = new Cart(userId);
            return cartRepository.save(newCart);
        }
    }

    private void recalculateTotalPrice(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        double totalPrice = 0.0;
        for (CartItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + item.getProductId()));
            totalPrice += product.getPrice() * item.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.updateTimestamp();
        cartRepository.save(cart);
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        List<CartItemResponse> itemResponses = new ArrayList<>();
        int totalItems = 0;

        for (CartItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + item.getProductId()));

            ProductResponse productResponse = ProductResponse.fromProduct(product);
            double subtotal = product.getPrice() * item.getQuantity();

            CartItemResponse itemResponse = new CartItemResponse(
                item.getId(),
                productResponse,
                item.getQuantity(),
                subtotal,
                item.getSelectedSize(), item.getSelectedColor()
            );

            itemResponses.add(itemResponse);
            totalItems += item.getQuantity();
        }

        return new CartResponse(
            cart.getId(),
            cart.getUserId(),
            itemResponses,
            cart.getTotalPrice(),
            totalItems,
            cart.getUpdatedAt()
        );
    }
}
