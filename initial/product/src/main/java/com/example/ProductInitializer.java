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
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Initializes some products in database.
 */
@Component
public class ProductInitializer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ProductInitializer.class.getName());

    // Identity Provider: Auth0
    private static final String STANDARD_USER_FIRST_NAME = "Uwe";
    private static final String STANDARD_USER_LAST_NAME = "User";
    private static final String ADMIN_USER_FIRST_NAME = "Alex";
    private static final String ADMIN_USER_LAST_NAME = "Admin";

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
                                UUID.randomUUID().toString(),
                                STANDARD_USER_FIRST_NAME,
                                STANDARD_USER_LAST_NAME,
                                passwordEncoder.encode("user_4demo!"),
                                "user@example.com",
                                Collections.singletonList("USER")),
                        new ProductUserEntity(
                                UUID.randomUUID().toString(),
                                ADMIN_USER_FIRST_NAME,
                                ADMIN_USER_LAST_NAME,
                                passwordEncoder.encode("admin_4demo!"),
                                "admin@example.com",
                                Arrays.asList("USER", "ADMIN")))
                .forEach(productUserEntityRepository::save);

        LOG.info("Created " + productUserEntityRepository.count() + " users");
    }
}
