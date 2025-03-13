package dev.goral.rpgmanager.user.additional;

import dev.goral.rpgmanager.email.EmailService;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static dev.goral.rpgmanager.user.register.RegisterService.validatePassword;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    UserRepository userRepository;
    EmailService emailService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Map<String, Object> resetPassword(Map<String, String> email) {
        //check if email exists
        Optional<User> user = userRepository.findByEmail(email.get("email"));
        if (user.isEmpty()) {
            throw new IllegalStateException("Nie znaleziono użytkownika o podanym adresie e-mail.");
        }
        //generate token
        String token = UUID.randomUUID().toString();
        //save token to user
        User userToUpdate = user.get();
        userToUpdate.setToken(token);
        userRepository.save(userToUpdate);
        //send email with token
        emailService.sendResetPasswordEmail(userToUpdate);
        //return response
        return CustomReturnables.getOkResponseMap("E-mail z linkiem do resetowania hasła został wysłany.");
    }

    public Map<String, Object> validateToken(String token) {
        //check if token exists
        Optional<User> user = userRepository.findByToken(token);
        if (user.isEmpty()) {
            throw new IllegalStateException("Nie znaleziono użytkownika o podanym tokenie.");
        }
        //return response
        return CustomReturnables.getOkResponseMap("Token jest poprawny.");
    }

    public Map<String, Object> changePassword(String token, Map<String, String> password) {
        //check if token exists
        Optional<User> user = userRepository.findByToken(token);
        if (user.isEmpty()) {
            throw new IllegalStateException("Nie znaleziono użytkownika o podanym tokenie.");
        }

        //check if password is valid
        if (!validatePassword(password.get("password"))) {
            throw new IllegalStateException("Hasło musi zawierać co najmniej 8 znaków, jedną cyfrę, jedną małą literę, jedną dużą literę oraz jeden znak specjalny.");
        }
        //change password
        User userToUpdate = user.get();
        String passwordString = password.get("password");
        String encryptedPassword = bCryptPasswordEncoder.encode(passwordString);
        userToUpdate.setPassword(encryptedPassword);
        userToUpdate.setToken(null);
        userRepository.save(userToUpdate);
        //return response
        return CustomReturnables.getOkResponseMap("Hasło zostało zmienione.");
    }
}
