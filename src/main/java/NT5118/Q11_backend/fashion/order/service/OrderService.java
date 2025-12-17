package NT5118.Q11_backend.fashion.order.service;

import java.util.List;
import java.util.Optional;

import NT5118.Q11_backend.fashion.order.dto.OrderRequest;
import NT5118.Q11_backend.fashion.order.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    Optional<OrderResponse> getOrderById(String id);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByUserId(String userId);
    List<OrderResponse> getOrdersByStatus(String status);
    OrderResponse updateOrder(String id, OrderRequest request);
    void deleteOrder(String id);
}

