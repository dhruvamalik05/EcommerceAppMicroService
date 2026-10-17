package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.*;
//import com.ecommerce.order.repository.OrderRepository;
//import com.ecommerce.order.repository.UserRepository;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartservice;
//    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
//
        List<CartItem> cartItemList = cartservice.getCart(userId);
        if (cartItemList.isEmpty()) {
            Optional.empty();
        }

//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()) {
//            Optional.empty();
//        }
//        User user = userOptional.get();

        BigDecimal totalPrice = cartItemList.stream()
                .map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
//        order.setUser(user);
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItemList.stream()
                .map(item -> new OrderItem(null,
//                        item.getProduct(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order)).toList();


        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartservice.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getItems().stream()
                        .map(item-> new OrderItemDTO(
                                item.getId(),
//                                item.getProduct().getId(),
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice()
                        )).toList(),
                order.getCreatedAt()
        );
    }
}
