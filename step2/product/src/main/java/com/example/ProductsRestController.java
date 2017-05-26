package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest API for {@link Product products}.
 */
@RestController
public class ProductsRestController {

    private final ProductsRepository productsRepository;

    public ProductsRestController(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @GetMapping(path = "/products")
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }
}
