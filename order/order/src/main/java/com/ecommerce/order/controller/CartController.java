package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@RefreshScope
public class CartController {

    private final CartService cartservice;

    @PostMapping
    public ResponseEntity<HttpStatus> addToCart (@RequestHeader("X-User-ID") String userId,
                                                 @RequestBody CartItemRequest cartItemRequest) {
        boolean added = cartservice.addToCart(userId, cartItemRequest);
        if (!added) {
            ResponseEntity.badRequest().body("Product out of stock or product not found or user not found");
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpStatus> removeFromCart (@RequestHeader("X-User-ID") String userId,
                                                 @PathVariable Long productId) {
        boolean added = cartservice.removeFromCart(userId, productId);
        if (!added) {
            ResponseEntity.badRequest().body("Product out of stock or product not found or user not found");
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart (@RequestHeader("X-User-ID") String userId) {
        List<CartItem> cart = cartservice.getCart(userId);
        return ResponseEntity.ok(cart);
    }

}
