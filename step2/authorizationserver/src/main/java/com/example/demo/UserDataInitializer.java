package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Initializes some users with passwords.
 */
@Component
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserDataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of(
                new User("Hans", "Mustermann",
                        "user@example.com", "secret"),
                new User("Super", "Admin",
                        "admin@example.com", "geheim")
        ).forEach(userRepository::save);
    }
}
