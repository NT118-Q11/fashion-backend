package NT5118.Q11_backend.fashion.cart.repository;

import NT5118.Q11_backend.fashion.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    
    // Find cart by user ID
    Optional<Cart> findByUserId(String userId);
    
    // Check if cart exists for user
    boolean existsByUserId(String userId);
}
