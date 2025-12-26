package NT5118.Q11_backend.fashion.order.service;

import NT5118.Q11_backend.fashion.order.dto.OrderItemRequest;
import NT5118.Q11_backend.fashion.order.dto.OrderItemResponse;
import NT5118.Q11_backend.fashion.order.model.OrderItem;
import NT5118.Q11_backend.fashion.order.repository.OrderItemRepository;
import NT5118.Q11_backend.fashion.product.model.Product;
import NT5118.Q11_backend.fashion.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderItemResponse createOrderItem(OrderItemRequest request) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(request.getOrderId());
        orderItem.setProductId(request.getProductId());
        orderItem.setQuantity(request.getQuantity());
        orderItem.setPriceAtPurchase(request.getPriceAtPurchase());

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return mapToResponse(savedOrderItem);
    }

    @Override
    public Optional<OrderItemResponse> getOrderItemById(String id) {
        return orderItemRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    public List<OrderItemResponse> getAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByOrderId(String orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByProductId(String productId) {
        return orderItemRepository.findByProductId(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponse updateOrderItem(String id, OrderItemRequest request) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));

        orderItem.setOrderId(request.getOrderId());
        orderItem.setProductId(request.getProductId());
        orderItem.setQuantity(request.getQuantity());
        orderItem.setPriceAtPurchase(request.getPriceAtPurchase());

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return mapToResponse(updatedOrderItem);
    }

    @Override
    public void deleteOrderItem(String id) {
        orderItemRepository.deleteById(id);
    }

    private OrderItemResponse mapToResponse(OrderItem orderItem) {
        String productName = null;
        String size = null;
        String color = null;

        // Lấy thông tin product
        Optional<Product> productOpt = productRepository.findById(orderItem.getProductId());
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            productName = product.getName();
            size = product.getSize();
            color = product.getColor();
        }

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getOrderId(),
                orderItem.getProductId(),
                productName,
                size,
                color,
                orderItem.getQuantity(),
                orderItem.getPriceAtPurchase()
        );
    }
}

