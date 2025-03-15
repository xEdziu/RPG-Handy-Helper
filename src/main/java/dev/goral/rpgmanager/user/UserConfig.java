package dev.goral.rpgmanager.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.smartcardio.CommandAPDU;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

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
            userRepository.save(admin);
        };
    }
}
