package NT5118.Q11_backend.fashion.cart.repository;

import NT5118.Q11_backend.fashion.cart.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {
    
    // Find all items in a cart
    List<CartItem> findByCartId(String cartId);
    
    // Find specific item in cart by product
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);
    
    // Delete all items in a cart
    void deleteByCartId(String cartId);
    
    // Count items in a cart
    long countByCartId(String cartId);
}
