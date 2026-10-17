package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Optional<ProductResponse> createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    private Optional<ProductResponse> mapToProductResponse(Product savedProduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setActive(savedProduct.getActive());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        productResponse.setName(savedProduct.getName());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setStockQuantity(savedProduct.getStockQuantity());
        return Optional.of(productResponse);

    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {

        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setName(productRequest.getName());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());

    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(eu -> {updateProductFromRequest(eu, productRequest);
                Product savedproduct = productRepository.save(eu);
                return mapToProductResponse(savedproduct);
                }).orElseThrow();

    }

    public List<Optional<ProductResponse>> getProduct() {
        return productRepository.findByActiveTrue().stream()
                .map(u->mapToProductResponse((Product) u)).toList();
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(u->{
                    u.setActive(false);
                    productRepository.save(u);
                    return true;
                }).orElse(false);

    }

    public List<Optional<ProductResponse>> searchProduct(String keyword) {
        return productRepository.searchProduct(keyword).stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }
}
