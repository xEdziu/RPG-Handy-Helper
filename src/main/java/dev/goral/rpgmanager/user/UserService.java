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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

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
            return new UserDTO(foundUser.getId(), foundUser.getUsername(), foundUser.getFirstName(), foundUser.getSurname(), foundUser.getEmail(), foundUser.getUserPhotoPath());
        } else if (principal instanceof DefaultOAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getSurname(), user.getEmail(), user.getUserPhotoPath());
            } else {
                throw new IllegalStateException("Nie udało się znaleźć użytkownika w bazie danych.");
            }
        } else {
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
        }
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

    public Map<String, Object> updateProfile(UserUpdateRequest updateRequest, @AuthenticationPrincipal Object principal) {
        if (principal instanceof User user) {
            updateUserDetails(user, updateRequest);
        } else if (principal instanceof DefaultOAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika powiązanego z tym kontem Discord"));
            updateUserDetails(user, updateRequest);
        } else {
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
        }

        return CustomReturnables.getOkResponseMap("Profil zaktualizowany.");
    }

    private void updateUserDetails(User user, UserUpdateRequest updateRequest) {
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

        if (updateRequest.getUsername().length() < 3 || updateRequest.getUsername().length() > 50) {
            throw new IllegalStateException("Nick musi mieć od 3 do 50 znaków.");
        }

        if (updateRequest.getFirstName().length() < 3 || updateRequest.getFirstName().length() > 50) {
            throw new IllegalStateException("Imię musi mieć od 3 do 50 znaków.");
        }

        if (updateRequest.getSurname().length() < 3 || updateRequest.getSurname().length() > 50) {
            throw new IllegalStateException("Nazwisko musi mieć od 3 do 50 znaków.");
        }

        user.setUsername(updateRequest.getUsername());
        user.setFirstName(updateRequest.getFirstName());
        user.setSurname(updateRequest.getSurname());

        userRepository.save(user);
    }

    @Transactional
    public Map<String, Object> uploadUserPhoto(MultipartFile file, @AuthenticationPrincipal Object principal) {
        try {
            if (file.isEmpty()) {
                throw new IllegalStateException("Nie wybrano pliku.");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalStateException("Plik jest za duży. Maksymalny rozmiar to 5 MB.");
            }

            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                throw new IllegalStateException("Nieprawidłowy typ pliku. Dozwolone są tylko JPEG i PNG.");
            }

            User user;
            if (principal instanceof User foundUser) {
                user = foundUser;
            } else if (principal instanceof DefaultOAuth2User oauthUser) {
                String email = oauthUser.getAttribute("email");
                user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika powiązanego z tym kontem Discord"));
            } else {
                throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
            }

            // Usuń poprzednie zdjęcie, jeśli nie domyślne
            String oldPath = user.getUserPhotoPath();
            if (oldPath != null &&
                    !oldPath.equals("/img/profilePics/defaultProfilePic.png") &&
                    !oldPath.equals("/img/profilePics/cyberpunkDefaultProfilePic.png")) {

                Path baseDir = Paths.get("src/main/resources/static/img/profilePics").normalize().toAbsolutePath();
                String oldFilename = Paths.get(oldPath).getFileName().toString();
                Path oldFile = baseDir.resolve(oldFilename).normalize().toAbsolutePath();

                if (!oldFile.startsWith(baseDir)) {
                    throw new IllegalStateException("Invalid file path");
                }

                Files.deleteIfExists(oldFile);
            }

            // Nowa nazwa pliku
            String filename = UUID.randomUUID() + ".webp";
            Path filepath = Paths.get("src/main/resources/static/img/profilePics", filename);
            Files.createDirectories(filepath.getParent());

            // Konwersja i zapis do WebP
            BufferedImage image = ImageIO.read(file.getInputStream());
            BufferedImage resized = resizeImage(image, 512, 512);

            try (ImageOutputStream output = ImageIO.createImageOutputStream(Files.newOutputStream(filepath))) {

                if (!ImageIO.getImageWritersByFormatName("webp").hasNext()) {
                    throw new IllegalStateException("Nie można znaleźć ImageWriter dla formatu WebP.");
                }

                ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
                writer.setOutput(output);

                ImageWriteParam param = writer.getDefaultWriteParam();
                writer.write(null, new IIOImage(resized, null, null), param);
                writer.dispose();
            }

            user.setUserPhotoPath("/img/profilePics/" + filename);
            userRepository.save(user);

            return CustomReturnables.getOkResponseMap("Zdjęcie profilowe zaktualizowane.");
        } catch (IOException e) {
            throw new IllegalStateException("Błąd przy zapisie zdjęcia");
        }
    }


    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public ResponseEntity<byte[]> getUserPhoto(String filename) throws IOException {
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalStateException("Invalid filename");
        }

        Path path = Paths.get("src/main/resources/static/img/profilePics").toAbsolutePath().normalize();
        Path photoPath = path.resolve(filename).normalize();

        if (!photoPath.startsWith(path)) {
            throw new IllegalStateException("Invalid filename");
        }

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
