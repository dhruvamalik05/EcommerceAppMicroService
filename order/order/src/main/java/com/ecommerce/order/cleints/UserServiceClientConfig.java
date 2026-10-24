package com.ecommerce.order.cleints;

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
public class UserServiceClientConfig {

    @Bean
    public UserServiceClient userClientHttpInterface(@LoadBalanced RestClient.Builder builder) {

        RestClient RestClient = builder
                .baseUrl("http://user-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (((request, response) -> Optional.empty())))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(RestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        UserServiceClient service = factory.createClient(UserServiceClient.class);
        return service;
    }
}
