package com.example.security;

import com.example.productuser.ProductUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ProductJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final UserDetailsService userDetailsService;

  @Autowired
  public ProductJwtAuthenticationConverter(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
    if (userDetails instanceof ProductUser) {
      return new ProductUserAuthenticationToken((ProductUser) userDetails, userDetails.getAuthorities());
    } else {
      return null;
    }
  }
}
