package dev.goral.rpgmanager.user.register;

import dev.goral.rpgmanager.email.EmailService;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.user.OAuthProvider;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Potwierdzanie adresu e-mail za pomocą tokena
     */
    public Map<String, Object> confirmEmail(String token) {
        Optional<User> userOptional = userRepository.findByToken(token);

        if (userOptional.isEmpty()) {
            throw new IllegalStateException("Nieprawidłowy lub nieaktywny token weryfikacyjny");
        }

        User user = userOptional.get();
        user.setEnabled(true);
        user.setToken(null); // Usuwamy token po weryfikacji
        userRepository.save(user);

        return CustomReturnables.getOkResponseMap("Konto zostało aktywowane. Możesz się teraz zalogować.");
    }

    public Map<String, Object> register(RegisterRequest request) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new IllegalStateException("Nazwa użytkownika jest już zajęta.");
            } else if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalStateException("Ten adres e-mail jest już zarejestrowany.");
            }

            if (!validatePassword(request.getPassword())) {
                throw new IllegalStateException("Hasło musi zawierać co najmniej 8 znaków, jedną cyfrę, jedną małą literę, jedną dużą literę oraz jeden znak specjalny.");
            }

            String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
            String token = UUID.randomUUID().toString();

            User newUser = new User(request.getUsername(), request.getFirstName(), request.getSurname(), request.getEmail(), encodedPassword, UserRole.ROLE_USER, new Timestamp(System.currentTimeMillis()));
            newUser.setEnabled(false); // Konto nieaktywne do momentu potwierdzenia e-maila
            OAuthProvider emailProvider = OAuthProvider.EMAIL;
            newUser.setOAuthProvider(emailProvider);
            newUser.setToken(token);

            userRepository.save(newUser);
            emailService.sendVerificationEmail(newUser); // Wysyłanie maila z tokenem

            return CustomReturnables.getOkResponseMap("Konto zostało utworzone. Sprawdź swoją skrzynkę e-mail, aby aktywować konto.");
    }

    /**
     * Walidacja hasła
     * @param password Hasło do walidacji
     *                 Musi zawierać co najmniej 8 znaków, jedną cyfrę, jedną małą literę, jedną dużą literę oraz jeden znak specjalny
     * @return bool
     */
    private Boolean validatePassword(String password) {
      return password.length() >= 8 &&
              password.matches(".*\\d.*") &&
              password.matches(".*[a-z].*") &&
              password.matches(".*[A-Z].*") &&
              password.matches(".*[!@#$%^&*()-+].*");
    }
}
