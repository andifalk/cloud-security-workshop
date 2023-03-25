package com.example.productuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest API for {@link ProductUser users}.
 */
@RestController
public class ProductUserRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductUserRestController.class.getName());

    private final ProductUserService productUserService;

    public ProductUserRestController(ProductUserService productUserService) {
        this.productUserService = productUserService;
    }

    @GetMapping(path = "/users")
    public List<ProductUser> getAllProductUsers(@AuthenticationPrincipal(errorOnInvalidType = true) ProductUser productUser) {
        LOG.info("Return all registered users using authenticated user '" + productUser.getEmail() + "'");
        return productUserService.findAll();
    }
}
