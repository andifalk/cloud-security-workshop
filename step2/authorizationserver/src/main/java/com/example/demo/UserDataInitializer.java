package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Initializes some users with passwords.
 */
@Component
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of(
                new User("Hans", "Mustermann",
                        "user@example.com", passwordEncoder.encode("secret")),
                new User("Super", "Admin",
                        "admin@example.com", passwordEncoder.encode("geheim"))
        ).forEach(userRepository::save);
    }
}
