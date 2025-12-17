package NT5118.Q11_backend.fashion.order.repository;

import NT5118.Q11_backend.fashion.order.model.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
    List<OrderItem> findByOrderId(String orderId);
    List<OrderItem> findByProductId(String productId);
}

