package com.example.security;

import com.example.productuser.ProductUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductUserDetailsService implements UserDetailsService {

    private final ProductUserService productUserService;

    @Autowired
    public ProductUserDetailsService(ProductUserService productUserService) {
        this.productUserService = productUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return productUserService.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("No user could be found for user name '" + username + "'"));
    }
}
