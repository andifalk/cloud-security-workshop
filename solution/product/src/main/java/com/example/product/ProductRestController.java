package com.example.product;

import com.example.productuser.ProductUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Rest API for {@link Product products}. */
@RestController
public class ProductRestController {
  private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class.getName());

  private final ProductService productService;

  public ProductRestController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping(path = "/products")
  public List<Product> getAllProducts(@AuthenticationPrincipal(errorOnInvalidType = true) ProductUser productUser) {
    LOG.info("Return all products using authenticated user '" + productUser.getEmail() + "'");
    return productService.findAll();
  }
}
