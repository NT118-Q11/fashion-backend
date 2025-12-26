package NT5118.Q11_backend.fashion.order.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import NT5118.Q11_backend.fashion.order.dto.OrderRequest;
import NT5118.Q11_backend.fashion.order.dto.OrderResponse;
import NT5118.Q11_backend.fashion.order.dto.OrderItemResponse;
import NT5118.Q11_backend.fashion.order.model.Order;
import NT5118.Q11_backend.fashion.order.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus(request.getStatus());
        order.setShippingAddress(request.getShippingAddress());

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    @Override
    public Optional<OrderResponse> getOrderById(String id) {
        return orderRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrder(String id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setUserId(request.getUserId());
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus(request.getStatus());
        order.setShippingAddress(request.getShippingAddress());

        Order updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = orderItemService.getOrderItemsByOrderId(order.getId());
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getShippingAddress(),
                order.getCreatedAt(),
                items
        );
    }
}
