package com.example;

import com.example.product.Product;
import com.example.product.ProductRepository;
import com.example.productuser.ProductUser;
import com.example.productuser.ProductUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Stream;

/** Initializes some products in database. */
@Component
public class ProductInitializer implements CommandLineRunner {
  private static final Logger LOG = LoggerFactory.getLogger(ProductInitializer.class.getName());

  private final ProductRepository productRepository;
  private final ProductUserRepository productUserRepository;

  public ProductInitializer(
      ProductRepository productRepository, ProductUserRepository productUserRepository) {
    this.productRepository = productRepository;
    this.productUserRepository = productUserRepository;
  }

  @Override
  public void run(String... strings) {
    Stream.of(
            new Product("Apple", "A green apple", 3.50),
            new Product("Banana", "The perfect banana", 7.00),
            new Product("Orange", "Lots of sweet oranges", 33.00),
            new Product("Pineapple", "Exotic pineapple", 1.50),
            new Product("Grapes", "Red wine grapes", 10.75))
        .forEach(productRepository::save);

    LOG.info("Created " + productRepository.count() + " products");

    Stream.of(
            new ProductUser(
                "auth0|5bc44fceb144eb0173391741",
                "Uwe",
                "User",
                "n/a",
                "user@example.com",
                Collections.singletonList("USER")),
            new ProductUser(
                "auth0|5bc4b1553385d56f61f70e3b",
                "Alex",
                "Admin",
                "n/a",
                "admin@example.com",
                Collections.singletonList("ADMIN")))
        .forEach(productUserRepository::save);

    LOG.info("Created " + productUserRepository.count() + " users");
  }
}
