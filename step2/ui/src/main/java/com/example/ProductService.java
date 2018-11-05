package com.example;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Service
public class ProductService {

    private final OAuth2RestTemplate template;

    @Autowired
    public ProductService(OAuth2RestTemplate template) {
        this.template = template;
    }

    @HystrixCommand(fallbackMethod = "fallbackProducts",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            })
    public Collection<Product> getAllProducts() {

        ResponseEntity<Product[]> response = template.getForEntity(
                "http://localhost:8080/products", Product[].class);

        return Arrays.asList(response.getBody());
    }

    public Collection<Product> fallbackProducts() {
        return Collections.emptyList();
    }

}
