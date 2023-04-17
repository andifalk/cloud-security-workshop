package com.example;

import com.example.product.ProductEntity;
import com.example.product.ProductEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * Initializes some products in database.
 */
@Component
public class ProductInitializer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ProductInitializer.class.getName());

    private final ProductEntityRepository productEntityRepository;

    public ProductInitializer(
            ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @Override
    public void run(String... strings) {
        Stream.of(
                        new ProductEntity("Apple", "A green apple", BigDecimal.valueOf(3.50)),
                        new ProductEntity("Banana", "The perfect banana", BigDecimal.valueOf(7.00)),
                        new ProductEntity("Orange", "Lots of sweet oranges", BigDecimal.valueOf(33.00)),
                        new ProductEntity("Pineapple", "Exotic pineapple", BigDecimal.valueOf(1.50)),
                        new ProductEntity("Grapes", "Red wine grapes", BigDecimal.valueOf(10.75)))
                .forEach(productEntityRepository::save);

        LOG.info("Created " + productEntityRepository.count() + " products");
    }
}
