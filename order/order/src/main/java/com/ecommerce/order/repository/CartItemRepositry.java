package com.ecommerce.order.repository;

import com.ecommerce.order.model.CartItem;
//import com.ecommerce.order.model.Product;
//import com.ecommerce.order.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepositry extends JpaRepository<CartItem, Long> {

    CartItem findByUserIdAndProductId(String userId, String productId);

    void deleteByUserIdAndProductId(String userId, String productId);

    List<CartItem> findByUserId(String userId);

    void deleteByUserId(String userId);
}
