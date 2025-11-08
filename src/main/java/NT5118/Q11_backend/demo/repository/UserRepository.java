package NT5118.Q11_backend.demo.repository;

import NT5118.Q11_backend.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends MongoRepository<User, String> {
    // Custom finder methods can be added here, e.g.:
    // Optional<User> findByEmail(String email);
}
