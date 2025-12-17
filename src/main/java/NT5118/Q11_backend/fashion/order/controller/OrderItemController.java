package NT5118.Q11_backend.fashion.order.controller;

import NT5118.Q11_backend.fashion.order.dto.OrderItemRequest;
import NT5118.Q11_backend.fashion.order.dto.OrderItemResponse;
import NT5118.Q11_backend.fashion.order.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * Create a new order item
     * POST /api/order-items
     */
    @PostMapping
    public ResponseEntity<OrderItemResponse> createOrderItem(@Valid @RequestBody OrderItemRequest request) {
        OrderItemResponse response = orderItemService.createOrderItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all order items
     * GET /api/order-items
     */
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getAllOrderItems() {
        List<OrderItemResponse> orderItems = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Get order item by ID
     * GET /api/order-items/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable String id) {
        return orderItemService.getOrderItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all order items for a specific order
     * GET /api/order-items/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> getOrderItemsByOrderId(@PathVariable String orderId) {
        List<OrderItemResponse> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Get all order items for a specific product
     * GET /api/order-items/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItemResponse>> getOrderItemsByProductId(@PathVariable String productId) {
        List<OrderItemResponse> orderItems = orderItemService.getOrderItemsByProductId(productId);
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Update an order item
     * PUT /api/order-items/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderItem(@PathVariable String id, @Valid @RequestBody OrderItemRequest request) {
        try {
            OrderItemResponse response = orderItemService.updateOrderItem(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete an order item
     * DELETE /api/order-items/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable String id) {
        return orderItemService.getOrderItemById(id)
                .map(orderItem -> {
                    orderItemService.deleteOrderItem(id);
                    return ResponseEntity.ok(Map.of("message", "Order item deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

