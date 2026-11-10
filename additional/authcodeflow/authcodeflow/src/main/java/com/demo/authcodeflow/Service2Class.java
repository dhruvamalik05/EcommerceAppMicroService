package com.demo.authcodeflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Service2Class {
    public Service2Class(RestTemplate rest, OAuth2AuthorizedClientManager manager) {
        this.rest = rest;
        this.manager = manager;
    }

    private final RestTemplate rest;
    private final OAuth2AuthorizedClientManager manager;

    @Value("${service2.url}")
    String service2Url;

    public String fetchData() {

        var authRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak-client")
                .principal("machine")
                .build();

        var client = manager.authorize(authRequest);
        String token = client.getAccessToken().getTokenValue();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var resp = rest.exchange(service2Url + "/data",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        return resp.getBody();
    }


}
