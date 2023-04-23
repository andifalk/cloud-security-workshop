package com.example;

import com.example.product.ProductEntity;
import com.example.product.ProductEntityRepository;
import com.example.productuser.ProductUserEntity;
import com.example.productuser.ProductUserEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static java.util.UUID.randomUUID;

/**
 * Initializes some products in database.
 */
@Component
public class ProductInitializer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ProductInitializer.class.getName());

    private final ProductEntityRepository productEntityRepository;
    private final ProductUserEntityRepository productUserEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public ProductInitializer(
            ProductEntityRepository productEntityRepository,
            ProductUserEntityRepository productUserEntityRepository,
            PasswordEncoder passwordEncoder) {
        this.productEntityRepository = productEntityRepository;
        this.productUserEntityRepository = productUserEntityRepository;
        this.passwordEncoder = passwordEncoder;
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

        Stream.of(
                        new ProductUserEntity(
                                randomUUID().toString(),
                                "Bruce",
                                "Wayne",
                                passwordEncoder.encode("wayne"),
                                "bruce.wayne@example.com",
                                Collections.singletonList("USER")),
                        new ProductUserEntity(
                                randomUUID().toString(),
                                "Clark",
                                "Kent",
                                passwordEncoder.encode("kent"),
                                "clark.kent@example.com",
                                Collections.singletonList("USER")),
                        new ProductUserEntity(
                                randomUUID().toString(),
                                "Peter",
                                "Parker",
                                passwordEncoder.encode("parker"),
                                "peter.parker@example.com",
                                Arrays.asList("USER", "ADMIN")))
                .forEach(productUserEntityRepository::save);

        LOG.info("Created " + productUserEntityRepository.count() + " users");
    }
}
