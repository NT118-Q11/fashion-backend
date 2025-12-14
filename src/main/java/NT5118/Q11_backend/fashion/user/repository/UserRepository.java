package NT5118.Q11_backend.fashion.user.repository;

import NT5118.Q11_backend.fashion.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    
    @Query("{ 'phone_number': ?0 }")
    Optional<User> findByPhone_number(String phoneNumber);
}
