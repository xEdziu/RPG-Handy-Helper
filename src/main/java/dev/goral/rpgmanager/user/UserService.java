package dev.goral.rpgmanager.user;

import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.*;

import static dev.goral.rpgmanager.user.register.RegisterService.validatePassword;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "Nie znaleziono użytkownika o nicku %s";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Logowanie użytkowników przez Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));

        if (user.getPassword() == null) {
            throw new IllegalStateException("To konto zostało utworzone przez Discord. Musisz najpierw ustawić hasło.");
        }

        return user;
    }

    public UserDTO getAuthorizedUser() {
        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof User foundUser) {
            return new UserDTO(foundUser.getUsername(), foundUser.getFirstName(), foundUser.getSurname(), foundUser.getEmail(), foundUser.getUserPhotoPath());
        } else if (principal instanceof DefaultOAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return new UserDTO(user.getUsername(), user.getFirstName(), user.getSurname(), user.getEmail(), user.getUserPhotoPath());
            } else {
                throw new IllegalStateException("Nie udało się znaleźć użytkownika w bazie danych.");
            }
        }
        throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Map<String, Object> setPassword(String password) {
        Object principal = getAuthentication().getPrincipal();

        if (!validatePassword(password)) {
            throw new IllegalStateException("Hasło musi zawierać co najmniej 8 znaków, jedną cyfrę, jedną małą literę, jedną dużą literę oraz jeden znak specjalny.");
        }

        if (principal instanceof User foundUser) {
            if (foundUser.getPassword() != null) {
                throw new IllegalStateException("Hasło jest już ustawione. Użyj opcji resetowania hasła.");
            }
            foundUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(foundUser);
        } else if (principal instanceof DefaultOAuth2User DefaultOAuth2User) {
            User user = userRepository.findByOAuthId(DefaultOAuth2User.getAttribute("id"))
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika powiązanego z tym kontem Discord"));

            if (user.getPassword() != null) {
                throw new IllegalStateException("Hasło jest już ustawione. Użyj opcji resetowania hasła.");
            }
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        }

        return CustomReturnables.getOkResponseMap("Hasło zostało ustawione");
    }

    public Map<String, Object> setUserPhotoPath(String userPhotoPath) {
        User user = (User) getAuthentication().getPrincipal();
        user.setUserPhotoPath(userPhotoPath);
        userRepository.save(user);
        return CustomReturnables.getOkResponseMap("Zdjęcie profilowe zostało ustawione.");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Map<String, Object> createUserAdmin(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Użytkownik o podanym nicku już istnieje.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Użytkownik o podanym emailu już istnieje.");
        }
        if (userRepository.findByOAuthId(user.getOAuthId()).isPresent()) {
            throw new IllegalStateException("Użytkownik o podanym ID OAuth już istnieje.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalStateException("Hasło nie może być puste.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return CustomReturnables.getOkResponseMap("Użytkownik został utworzony.");
    }

    public Map<String, Object> updateProfile(UserUpdateRequest updateRequest, @AuthenticationPrincipal User user) {
        if (updateRequest.getUsername() == null || updateRequest.getUsername().isEmpty()) {
            throw new IllegalStateException("Nick nie może być pusty.");
        }

        if (userRepository.findByUsername(updateRequest.getUsername()).isPresent()
                && !Objects.equals(user.getUsername(), updateRequest.getUsername())) {
            throw new IllegalStateException("Użytkownik o podanym nicku już istnieje.");
        }

        if (updateRequest.getFirstName() == null || updateRequest.getFirstName().isEmpty()) {
            throw new IllegalStateException("Imię nie może być puste.");
        }

        if (updateRequest.getSurname() == null || updateRequest.getSurname().isEmpty()) {
            throw new IllegalStateException("Nazwisko nie może być puste.");
        }

        user.setUsername(updateRequest.getUsername());
        user.setFirstName(updateRequest.getFirstName());
        user.setSurname(updateRequest.getSurname());

        userRepository.save(user);

        return CustomReturnables.getOkResponseMap("Profil zaktualizowany.");
    }

    @Transactional
    public Map<String, Object> setUserPhoto(MultipartFile file, @AuthenticationPrincipal User user) {
        try {
            if (file.isEmpty()) {
                throw new IllegalStateException("Nie wybrano pliku.");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalStateException("Plik jest za duży. Maksymalny rozmiar to 5 MB.");
            }

            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                throw new IllegalStateException("Nieprawidłowy typ pliku. Dozwolone są tylko pliki JPEG i PNG.");
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get("src/main/resources/static/img/profilePics", filename);
            Files.createDirectories(filepath.getParent());
            Files.write(filepath, file.getBytes());

            user.setUserPhotoPath("/img/profilePics/" + filename);
            userRepository.save(user);

            return CustomReturnables.getOkResponseMap("Zdjęcie profilowe zaktualizowane.");
        } catch (IOException e) {
            throw new IllegalStateException("Błąd przy zapisie zdjęcia", e);
        }
    }

    public ResponseEntity<byte[]> getUserPhoto(String filename) throws IOException {
        Path photoPath = Paths.get("src/main/resources/static/img/profilePics").resolve(filename);

        if (!Files.exists(photoPath)) {
            throw new FileNotFoundException("Nie znaleziono pliku: " + filename);
        }

        byte[] image = Files.readAllBytes(photoPath);
        String contentType = Files.probeContentType(photoPath);
        MediaType mediaType = contentType != null ? MediaType.parseMediaType(contentType) : MediaType.IMAGE_PNG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(image);
    }
}
