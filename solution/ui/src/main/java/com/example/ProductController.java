package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UI controller for products frontend.
 */
@Controller
public class ProductController {

    private final ProductService productService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public ProductController(ProductService productService, OAuth2AuthorizedClientService authorizedClientService) {
        this.productService = productService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping(path = "/")
    public String index(@AuthenticationPrincipal OidcUser oidcUserInfo, Model model) {
        String fullName = oidcUserInfo.getUserInfo().getNickName();
        model.addAttribute("username", fullName);
        return "index";
    }

    @GetMapping(path = "/products")
    public String getAllProducts(Authentication authentication, Model model) {
        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient("auth0", authentication.getName());
        Iterable<Product> products = productService.getAllProducts(authorizedClient.getAccessToken());
        model.addAttribute("products", products);
        return "products";
    }
}
