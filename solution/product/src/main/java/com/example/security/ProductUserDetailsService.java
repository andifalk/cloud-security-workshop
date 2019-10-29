package com.example.security;

import com.example.productuser.ProductUser;
import com.example.productuser.ProductUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class ProductUserDetailsService implements UserDetailsService {

  private final ProductUserService productUserService;

  @Autowired
  public ProductUserDetailsService(ProductUserService productUserService) {
    this.productUserService = productUserService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    ProductUser user = productUserService.findByUserId(username);
    if (user == null) {
      throw new UsernameNotFoundException(
          "No user could be found for user name '" + username + "'");
    }

    return user;
  }
}
