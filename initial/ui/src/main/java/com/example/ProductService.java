package com.example;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Service
public class ProductService {

    private RestTemplate template = new RestTemplate();

    @HystrixCommand(fallbackMethod = "fallbackProducts")
    public Collection<Product> getAllProducts() {

        ResponseEntity<Product[]> response = template.getForEntity(
                "http://localhost:8080/products", Product[].class);

        return Arrays.asList(response.getBody());
    }

    public Collection<Product> fallbackProducts() {
        return Collections.emptyList();
    }

}
