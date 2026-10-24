package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RefreshScope 
public class OrderController {

    private final OrderService orderservice;

    @PostMapping
    public ResponseEntity<Optional<OrderResponse>> createOrder(@RequestHeader("X-User-ID") String userId) {
        Optional<OrderResponse> order = orderservice.createOrder(userId);
        return (order.isEmpty()) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(order);
    }



}
