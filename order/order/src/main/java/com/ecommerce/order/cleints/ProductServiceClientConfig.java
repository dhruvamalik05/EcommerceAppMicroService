package com.ecommerce.order.cleints;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {

    @Autowired(required = false)
    private ObservationRegistry observationRegistry;

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedWebClientBuilder() {
        return RestClient.builder();
    }

    // Plain (non-load-balanced) builder — used by Eureka and other internal Spring Cloud HTTP clients
    @Bean
    @Primary
    public RestClient.Builder normalWebClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public ProductServiceClient productClientHttpInterface(@LoadBalanced RestClient.Builder builder) {
        if (observationRegistry != null) {
            builder.observationRegistry(observationRegistry);
        }
        RestClient restClient = builder
                .baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> Optional.empty())
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ProductServiceClient.class);
    }
}
