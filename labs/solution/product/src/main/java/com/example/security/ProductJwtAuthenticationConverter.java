package com.example.security;

import com.example.productuser.ProductUser;
import com.example.productuser.ProductUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final List<String> ROLE_CLAIMS = List.of("roles", "permissions");
    private static final String FIRST_NAME_CLAIM = "given_name";
    private static final String LAST_NAME_CLAIM = "family_name";
    private static final String EMAIL_CLAIM = "email";

    private final ProductUserService productUserService;

    @Autowired
    public ProductJwtAuthenticationConverter(ProductUserService productUserService) {
        this.productUserService = productUserService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        ProductUser productUser = new ProductUser(
                jwt.getSubject(), jwt.getClaimAsString(FIRST_NAME_CLAIM), jwt.getClaimAsString(LAST_NAME_CLAIM),
                "n/a", jwt.getClaimAsString(EMAIL_CLAIM), getRolesFromToken(jwt));
        // register the user, so we know what users are known to our system
        if (productUserService.findByUserId(productUser.getUserId()).isEmpty()) {
            productUserService.save(productUser);
        }
        return new ProductUserAuthenticationToken(productUser, productUser.getAuthorities());
    }

    private List<String> getRolesFromToken(Jwt jwt) {
        List<String> roles = new ArrayList<>();
        for (String claim : ROLE_CLAIMS) {
            if (jwt.hasClaim(claim)) {
                roles.addAll(jwt.getClaimAsStringList(claim));
            }
        }
        return roles.stream().map(String::toUpperCase).toList();
    }
}
