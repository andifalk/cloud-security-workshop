package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.core.userdetails.User.builder;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain api(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
                .formLogin(withDefaults()).logout().invalidateHttpSession(true);
        return http.build();
    }

    @Bean
    public UserDetailsService productUserDetailsService() {
        return new InMemoryUserDetailsManager(
            builder()
                .username("bruce.wayne@example.com")
                .password("bruce_4demo!")
                .passwordEncoder(passwordEncoder()::encode)
                .roles("USER")
                .build(),
            builder()
                .username("peter.parker@example.com")
                .password("peter_4demo!")
                .passwordEncoder(passwordEncoder()::encode)
                .roles("USER", "ADMIN")
                .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
