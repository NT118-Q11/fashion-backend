package NT5118.Q11_backend.fashion.order.service;

import NT5118.Q11_backend.fashion.order.dto.OrderItemRequest;
import NT5118.Q11_backend.fashion.order.dto.OrderItemResponse;
import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    OrderItemResponse createOrderItem(OrderItemRequest request);
    Optional<OrderItemResponse> getOrderItemById(String id);
    List<OrderItemResponse> getAllOrderItems();
    List<OrderItemResponse> getOrderItemsByOrderId(String orderId);
    List<OrderItemResponse> getOrderItemsByProductId(String productId);
    OrderItemResponse updateOrderItem(String id, OrderItemRequest request);
    void deleteOrderItem(String id);
}

