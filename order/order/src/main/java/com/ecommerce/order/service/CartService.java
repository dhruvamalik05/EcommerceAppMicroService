package com.ecommerce.order.service;

import com.ecommerce.order.cleints.ProductServiceClient;
import com.ecommerce.order.cleints.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
//import com.ecommerce.order.model.Product;
//import com.ecommerce.order.model.User;
import com.ecommerce.order.repository.CartItemRepositry;
//import com.ecommerce.order.repository.ProductRepository;
//import com.ecommerce.order.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

//    private final ProductRepository productRepository; private final UserRepository userRepository;
    private final CartItemRepositry cartItemRepositry;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {

        ProductResponse product = productServiceClient.getProductById(cartItemRequest.getProductId());
        System.out.println(product);
        if (product == null) {
            System.out.println("product issue");
            return false;
        }
        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
            return false;
        }


        UserResponse user = userServiceClient.getUserById(userId);
        if (user == null) {
            System.out.println("user issue");
            return false;
        }

        String productId = cartItemRequest.getProductId().toString();
        CartItem existingCartItem = cartItemRepositry.findByUserIdAndProductId(userId, productId);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
//            existingCartItem.setPrice(BigDecimal.valueOf(10000000));
            cartItemRepositry.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
//            cartItem.setUser(user);
//            cartItem.setProduct(product);
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
//            cartItem.setPrice(BigDecimal.valueOf(10000000));
            cartItemRepositry.save(cartItem);
        }

        return true;

    }

    public boolean removeFromCart(String userId, Long productId) {

//        Optional<Product> productOpt = productRepository.findById(productId);
//        if (productOpt.isEmpty()) {return false;}
//        Product product = productOpt.get();
//
//        if (product.getStockQuantity() <= 0 ) {
//            return false;
//        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty()) {return false;}
//        User user = userOpt.get();

        cartItemRepositry.deleteByUserIdAndProductId(userId, productId.toString());

        return false;

    }
    public List<CartItem> getCart(String userId) {
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
//        }
//        User user = userOpt.get();
        return cartItemRepositry.findByUserId(userId);

    }

    public void clearCart(String userId) {
//        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepositry::deleteByUserId);
        cartItemRepositry.deleteByUserId(userId);
    }

}
