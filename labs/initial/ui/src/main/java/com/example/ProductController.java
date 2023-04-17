package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UI controller for products frontend.
 */
@Controller
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/")
    public String index(@AuthenticationPrincipal User user, Model model) {
        String fullName = user.getUsername();
        model.addAttribute("username", fullName);
        return "index";
    }

    @GetMapping(path = "/products")
    public String getAllProducts(Model model) {
        Iterable<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(path = "/users")
    public String getAllUsers(Model model) {
        Iterable<ProductUser> users = productService.getAllProductUsers();
        model.addAttribute("users", users);
        return "users";
    }

}
