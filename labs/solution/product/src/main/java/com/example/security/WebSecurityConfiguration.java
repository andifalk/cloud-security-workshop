package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

    @Order(1)
    @Bean
    public SecurityFilterChain docs(HttpSecurity http) throws Exception {
        http.securityMatcher("/v3/**", "/swagger-ui.html", "/swagger-ui/**", "/actuator/health", "/actuator/info")
                .httpBasic().disable().formLogin().disable()
                .authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
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
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(productJwtAuthenticationConverter);
        return http.build();
    }
}
