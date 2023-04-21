package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
    private final OAuth2AuthorizedClientService authorizedClientService;

    public ProductController(ProductService productService, OAuth2AuthorizedClientService authorizedClientService) {
        this.productService = productService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping(path = "/")
    public String index(@AuthenticationPrincipal OidcUser oidcUserInfo, Model model) {
        String fullName = oidcUserInfo.getUserInfo().getFullName();
        model.addAttribute("username", fullName);
        return "index";
    }

    @GetMapping(path = "/products")
    public String getAllProducts(Authentication authentication, Model model) {
        OAuth2AccessToken accessToken = resolveAccessToken(authentication);
        Iterable<Product> products = productService.getAllProducts(accessToken);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(path = "/users")
    public String getAllUsers(Authentication authentication, Model model) {
        OAuth2AccessToken accessToken = resolveAccessToken(authentication);
        Iterable<ProductUser> users = productService.getAllProductUsers(accessToken);
        model.addAttribute("users", users);
        return "users";
    }

    private OAuth2AccessToken resolveAccessToken(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            OAuth2AuthorizedClient authorizedClient =
                    this.authorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), authentication.getName());
            if (authorizedClient != null) {
                LOG.info("Client is authorized {}", authorizedClient.getPrincipalName());
                return authorizedClient.getAccessToken();
            } else {
                LOG.warn("Client is not authorized");
                throw new BadCredentialsException("Invalid Token");
            }
        } else {
            LOG.warn("Unexpected authentication type {}", authentication);
            throw new BadCredentialsException("Invalid Token");
        }
    }
}
