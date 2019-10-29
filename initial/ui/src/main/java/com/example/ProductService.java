package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

@Service
public class ProductService {

  private final RestTemplate template;

  @Autowired
  public ProductService(RestTemplate template) {
    this.template = template;
  }

  public Collection<Product> getAllProducts() {

    ResponseEntity<Product[]> response =
        template.exchange(
            "http://localhost:9090/server/products",
            HttpMethod.GET,
            new HttpEntity<Product[]>(createAuthorizationHeader()),
            Product[].class);

    if (response.getBody() != null) {
      return Arrays.asList(response.getBody());
    } else {
      return Collections.emptyList();
    }
  }

  private HttpHeaders createAuthorizationHeader() {
    return new HttpHeaders() {
      {
        String auth = "user@example.com:secret";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        set("Authorization", authHeader);
      }
    };
  }
}
