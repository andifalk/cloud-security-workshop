package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

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
        http.httpBasic().and().formLogin()
                .and().authorizeHttpRequests().anyRequest().fullyAuthenticated();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
