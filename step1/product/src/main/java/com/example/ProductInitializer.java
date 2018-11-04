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
                new Product("Apple", "A green apple", 3.50),
                new Product("Banana", "The perfect banana", 7.00),
                new Product("Orange", "Lots of sweet oranges", 33.00),
                new Product("Pineapple", "Exotic pineapple", 1.50),
                new Product("Grapes", "Red wine grapes", 10.75)
        ).forEach(productsRepository::save);
    }
}
