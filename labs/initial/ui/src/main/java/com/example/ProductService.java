package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getEncoder;
import static org.springframework.http.HttpMethod.GET;

@Service
public class ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private final UserDetailsService userDetailsService;
    private final String productUrl;
    private final RestTemplate template = new RestTemplate();

    public ProductService(@Value("${product.server.url}") String productUrl, UserDetailsService userDetailsService) {
        this.productUrl = productUrl;
        this.userDetailsService = userDetailsService;
    }

    public Collection<Product> getAllProducts() {

        ResponseEntity<Product[]> response;

        LOG.info("Calling product server at [{}]", productUrl);

        try {
            response = template.exchange(
                    productUrl + "/v1/products",
                    GET,
                    new HttpEntity<Product[]>(createAuthorizationHeader()),
                    Product[].class);

            LOG.info("Successfully called product server products rest API");

            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException ex) {
            LOG.error("Error calling called product server products rest API", ex);
            if (ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401)) || ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(403))) {
                throw new AccessDeniedException("You are not authorized to call this");
            } else {
                throw ex;
            }
        }
    }

    public Collection<ProductUser> getAllProductUsers() {

        ResponseEntity<ProductUser[]> response;

        LOG.info("Calling product server at [{}]", productUrl);

        try {
            response = template.exchange(
                    productUrl + "/v1/users",
                    GET,
                    new HttpEntity<ProductUser[]>(createAuthorizationHeader()),
                    ProductUser[].class);

            LOG.info("Successfully called product server user rest API");

            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException ex) {
            LOG.error("Error calling called product server user rest API", ex);
            if (ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401)) || ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(403))) {
                throw new AccessDeniedException("You are not authorized to call this");
            } else {
                throw ex;
            }
        }
    }

    private HttpHeaders createAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String password = userDetails.getPassword();
        return new HttpHeaders() {
            {
                String auth = String.format("%s:%s", username, password);
                byte[] encodedAuth = getEncoder().encode(auth.getBytes(UTF_8));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
}
