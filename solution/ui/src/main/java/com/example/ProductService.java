package com.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Service
public class ProductService {

    public Collection<Product> getAllProducts(OAuth2AccessToken oAuth2AccessToken) {

        RestTemplate template = new RestTemplate();

        ResponseEntity<Product[]> response =
                template.exchange(
                        "http://localhost:9090/server/products",
                        HttpMethod.GET,
                        new HttpEntity<Product[]>(createAuthorizationHeader(oAuth2AccessToken)),
                        Product[].class);

        if (response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("serial")
    private HttpHeaders createAuthorizationHeader(OAuth2AccessToken oAuth2AccessToken) {
        return new HttpHeaders() {
            {
                String authHeader = "Bearer " + oAuth2AccessToken.getTokenValue();
                set("Authorization", authHeader);
            }
        };
    }
}
