package dev.goral.rpgmanager.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.Date;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunnerForUser(UserRepository userRepository) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return args -> {
            User admin = new User(
                    "zuber",
                    "Mateusz",
                    "Zubrzycki",
                    "default.admin@gmail.com",
                    passwordEncoder.encode("adminPassword"),
                    UserRole.ROLE_ADMIN,
                    new Timestamp(new Date().getTime())
            );
            admin.setEnabled(true);

            User user1 = new User(
                    "user1",
                    "John",
                    "Doe",
                    "john.doe@example.com",
                    passwordEncoder.encode("password123"),
                    UserRole.ROLE_USER,
                    new Timestamp(new Date().getTime())
            );
            user1.setEnabled(true);

            User user2 = new User(
                    "user2",
                    "Jane",
                    "Smith",
                    "jane.smith@example.com",
                    passwordEncoder.encode("password123"),
                    UserRole.ROLE_USER,
                    new Timestamp(new Date().getTime())
            );
            user2.setEnabled(true);

            User user3 = new User(
                    "alice_jones",
                    "Alice",
                    "Jones",
                    "alice.jones@example.com",
                    passwordEncoder.encode("password123"),
                    UserRole.ROLE_USER,
                    new Timestamp(new Date().getTime())
            );
            user3.setEnabled(true);

            userRepository.save(admin);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(admin);
        };
    }
}
