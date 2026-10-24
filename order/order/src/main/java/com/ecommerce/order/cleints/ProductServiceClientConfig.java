package com.ecommerce.order.cleints;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {
    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedWebClientBuilder() {
        return RestClient.builder();
    }

    // 2. A fallback plain builder to prevent Eureka from breaking
    @Bean
    @Primary
    public RestClient.Builder normalWebClientBuilder() {
        return RestClient.builder();
    }


    @Bean
    public ProductServiceClient productClientHttpInterface(@LoadBalanced RestClient.Builder builder) {

        RestClient RestClient = builder
                .baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (((request, response) -> Optional.empty())))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(RestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        ProductServiceClient service = factory.createClient(ProductServiceClient.class);
        return service;
    }
}
