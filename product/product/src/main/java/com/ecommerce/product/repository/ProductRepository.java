package com.ecommerce.product.repository;

import com.ecommerce.product.model.Product;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    Collection<Object> findByActiveTrue();

    @Query("SELECT p FROM product_table p WHERE p.active = true AND p.stockQuantity > 0 AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProduct(@Param("keyword") String keyword);

}
