package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Initializes some products in database.
 */
@Component
public class ProductInitializer implements CommandLineRunner {

    private final ProductsRepository productsRepository;

    public ProductInitializer(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of(
                new Product("Product 1", "Description 1", 3.50),
                new Product("Product 2", "Description 2", 7.00),
                new Product("Product 3", "Description 3", 33.00),
                new Product("Product 4", "Description 4", 0.50),
                new Product("Product 5", "Description 5", 25.75)
        ).forEach(productsRepository::save);
    }
}
