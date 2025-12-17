package NT5118.Q11_backend.fashion.order.repository;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import NT5118.Q11_backend.fashion.order.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByStatus(String status);
    List<Order> findByUserId(String userId);

}


