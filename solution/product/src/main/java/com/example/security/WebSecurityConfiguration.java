package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

    private final ProductJwtAuthenticationConverter productJwtAuthenticationConverter;

    public WebSecurityConfiguration(ProductJwtAuthenticationConverter productJwtAuthenticationConverter) {
        this.productJwtAuthenticationConverter = productJwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain api(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .httpBasic()
                .disable()
                .formLogin()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(productJwtAuthenticationConverter);
        return http.build();
    }
}
