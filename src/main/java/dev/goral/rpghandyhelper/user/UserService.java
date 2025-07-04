package dev.goral.rpghandyhelper.user;

import dev.goral.rpghandyhelper.game.GameService;
import dev.goral.rpghandyhelper.notes.GameNoteService;
import dev.goral.rpghandyhelper.scheduler.service.SchedulerService;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

import static dev.goral.rpghandyhelper.user.register.RegisterService.validatePassword;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "Nie znaleziono użytkownika o nicku %s";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GameNoteService gameNoteService;
    private final GameService gameUserService;
    private final SchedulerService schedulerService;

    @Value("${userUploads}")
    private String userUploads;

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        switch (principal) {
            case User foundUser -> {
                return new UserDTO(foundUser.getId(), foundUser.getUsername(), foundUser.getFirstName(), foundUser.getSurname(), foundUser.getEmail(), foundUser.getUserPhotoPath());
            }
            case Jwt jwt -> {
                String username = jwt.getClaimAsString("sub");
                User userEntity = userRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika: " + username));
                return new UserDTO(
                        userEntity.getId(),
                        userEntity.getUsername(),
                        userEntity.getFirstName(),
                        userEntity.getSurname(),
                        userEntity.getEmail(),
                        userEntity.getUserPhotoPath()
                );
            }
            case DefaultOAuth2User oauthUser -> {
                String email = oauthUser.getAttribute("email");
                Optional<User> userOptional = userRepository.findByEmail(email);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getSurname(), user.getEmail(), user.getUserPhotoPath());
                } else {
                    throw new IllegalStateException("Nie udało się znaleźć użytkownika w bazie danych.");
                }
            }
            case null, default -> throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
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
        String oldPhotoPath = user.getUserPhotoPath();

        // Usuń poprzednie zdjęcie, jeśli istnieje i nie jest domyślne
        if (oldPhotoPath != null && !oldPhotoPath.startsWith("/img/profilePics/defaultProfilePic")) {
            Path uploadsDir = Paths.get(userUploads).normalize().toAbsolutePath();
            Path oldFile = uploadsDir.resolve(Paths.get(oldPhotoPath).getFileName().toString()).normalize();

            if (Files.exists(oldFile) && oldFile.startsWith(uploadsDir)) {
                try {
                    Files.delete(oldFile);
                } catch (IOException e) {
                    throw new IllegalStateException("Nie udało się usunąć poprzedniego zdjęcia profilowego.", e);
                }
            }
        }

        // Ustaw nowe zdjęcie
        user.setUserPhotoPath(userPhotoPath);
        userRepository.save(user);
        return CustomReturnables.getOkResponseMap("Zdjęcie profilowe zostało ustawione.");
    }

    public Map<String, Object> getAllUsers() {
        List<User> users = userRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano listę użytkowników.");
        response.put("users", users);
        return response;
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
            String extension;
            String formatName;
            if ("image/jpeg".equals(contentType)) {
                extension = ".jpg";
                formatName = "jpg";
            } else if ("image/png".equals(contentType)) {
                extension = ".png";
                formatName = "png";
            } else {
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

            // Usuń poprzednie zdjęcie, jeśli nie jest domyślne
            String oldPath = user.getUserPhotoPath();
            if (oldPath != null && !oldPath.startsWith("/img/profilePics/defaultProfilePic")) {
                Path uploadsDir = Paths.get(userUploads).normalize().toAbsolutePath();
                String oldFilename = Paths.get(oldPath).getFileName().toString();
                Path oldFile = uploadsDir.resolve(oldFilename).normalize().toAbsolutePath();

                if (!oldFile.startsWith(uploadsDir)) {
                    throw new IllegalStateException("Invalid file path");
                }

                Files.deleteIfExists(oldFile);
            }

            // Nowa nazwa pliku
            String filename = UUID.randomUUID() + extension;
            Path filepath = Paths.get(userUploads).resolve(filename);
            Files.createDirectories(filepath.getParent());

            // Przetwarzanie obrazu
            BufferedImage image = ImageIO.read(file.getInputStream());
            BufferedImage resized = resizeImage(image, formatName);
            ImageIO.write(resized, formatName, filepath.toFile());

            user.setUserPhotoPath("/uploads/" + filename);
            userRepository.save(user);

            return CustomReturnables.getOkResponseMap("Zdjęcie profilowe zaktualizowane.");
        } catch (IOException e) {
            throw new IllegalStateException("Błąd przy zapisie zdjęcia");
        }
    }

    public Map<String, Object> getDefaultProfilePics() {
        Path path = Paths.get("src/main/resources/static/img/profilePics").toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new IllegalStateException("Nie znaleziono katalogu ze zdjęciami profilowymi.");
        }
        List<String> defaultPics = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "defaultProfilePic*")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    defaultPics.add("/img/profilePics/" + entry.getFileName().toString());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Błąd przy odczycie katalogu ze zdjęciami profilowymi.");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano domyślne zdjęcia profilowe.");
        response.put("defaultProfilePics", defaultPics);
        return response;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, String extension) {
        Image tmp = originalImage.getScaledInstance(512, 512, Image.SCALE_SMOOTH);
        BufferedImage resized;
        if (extension.equals("png"))
            resized = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        else
            resized = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

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
            // check if it is in userUploads directory
            if (!Files.exists(Paths.get(userUploads).resolve(filename))) {
                throw new FileNotFoundException("Nie znaleziono pliku: " + filename);
            }
            photoPath = Paths.get(userUploads).resolve(filename).normalize();
        }

        byte[] image = Files.readAllBytes(photoPath);
        String contentType = Files.probeContentType(photoPath);
        MediaType mediaType = contentType != null ? MediaType.parseMediaType(contentType) : MediaType.IMAGE_PNG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(image);
    }

    public Map<String, Object> getUserPhotoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o nicku: " + username));

        String userPhotoPath = user.getUserPhotoPath();
        if (userPhotoPath == null || userPhotoPath.isEmpty()) {
            throw new ResourceNotFoundException("Użytkownik nie ma ustawionego zdjęcia profilowego.");
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano ścieżkę zdjęciową do profilu użytkownika.");
        response.put("userPhotoPath", userPhotoPath);
        return response;
    }

    public Map<String, Object> updateUserAdmin(Long id, UserUpdateAdminRequest updateRequest) {
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Użytkownik zaktualizowany.");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + id));

        if (updateRequest.getUsername() != null && !updateRequest.getUsername().isEmpty()) {
            if (userRepository.findByUsername(updateRequest.getUsername()).isPresent()
                    && !Objects.equals(user.getUsername(), updateRequest.getUsername())) {
                throw new IllegalStateException("Użytkownik o podanym nicku już istnieje.");
            }
            if (updateRequest.getUsername().length() < 3 || updateRequest.getUsername().length() > 50) {
                throw new IllegalStateException("Nick musi mieć od 3 do 50 znaków.");
            }
            user.setUsername(updateRequest.getUsername());
            if (updateRequest.getFirstName() != null && !updateRequest.getFirstName().isEmpty()) {
                if (updateRequest.getFirstName().length() < 3 || updateRequest.getFirstName().length() > 50) {
                    throw new IllegalStateException("Imię musi mieć od 3 do 50 znaków.");
                }
                user.setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getSurname() != null && !updateRequest.getSurname().isEmpty()) {
                if (updateRequest.getSurname().length() < 3 || updateRequest.getSurname().length() > 50) {
                    throw new IllegalStateException("Nazwisko musi mieć od 3 do 50 znaków.");
                }
                user.setSurname(updateRequest.getSurname());
            }
            if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
                if (userRepository.findByEmail(updateRequest.getEmail()).isPresent()
                        && !Objects.equals(user.getEmail(), updateRequest.getEmail())) {
                    throw new IllegalStateException("Użytkownik o podanym emailu już istnieje.");
                }
                user.setEmail(updateRequest.getEmail());
            }
            if (updateRequest.getRole() != null) {
                user.setRole(updateRequest.getRole());
            }
            if (updateRequest.getLocked() != null) {
                user.setLocked(updateRequest.getLocked());
            }
            if (updateRequest.getEnabled() != null) {
                user.setEnabled(updateRequest.getEnabled());
            }
            if (updateRequest.getUserPhotoPath() != null && !updateRequest.getUserPhotoPath().isEmpty()) {
                user.setUserPhotoPath(updateRequest.getUserPhotoPath());
            }
            userRepository.save(user);
        }
        return response;
    }

    public Map<String, Object> getAdminUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + id));

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano użytkownika.");
        response.put("user", user);
        return response;
    }

    @Transactional
    public Map<String, Object> deleteUserAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + id));

        // Usuń notatki powiązane z użytkownikiem
        gameNoteService.deleteAllByUserId(id);

        // Usuń zdjęcie profilowe, jeśli nie jest domyślne
        String userPhotoPath = user.getUserPhotoPath();
        if (userPhotoPath != null && !userPhotoPath.startsWith("/img/profilePics/defaultProfilePic")) {
            Path baseDir = Paths.get("src/main/resources/static/img/profilePics").normalize().toAbsolutePath();
            Path photoFile = baseDir.resolve(Paths.get(userPhotoPath).getFileName()).normalize().toAbsolutePath();
            try {
                Files.deleteIfExists(photoFile);
            } catch (IOException e) {
                throw new IllegalStateException("Błąd przy usuwaniu zdjęcia profilowego użytkownika.");
            }
        }

        // Usuń uczestnictwo w schedulerach
        schedulerService.removePlayerFromAllSchedulersById(id);

        // Usuń powiązania z grami
        gameUserService.deleteUserFromAllGames(id);

        // Usuń użytkownika
        userRepository.delete(user);

        return CustomReturnables.getOkResponseMap("Użytkownik został usunięty.");
    }

    public Map<String, Object> changeUserPasswordAdmin(Long id, String password) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + id));

        if (!validatePassword(password)) {
            throw new IllegalStateException("Hasło musi zawierać co najmniej 8 znaków, jedną cyfrę, jedną małą literę, jedną dużą literę oraz jeden znak specjalny.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        return CustomReturnables.getOkResponseMap("Twoje hasło zostało zmienione.");
    }
    public Map<String, Object> isPasswordSetForDiscordUser(Object user) {

        if (user instanceof User) {
            return CustomReturnables.getOkResponseMap("Użytkownik nie jest zalogowany przez OAuth2.");
        } else if (user instanceof DefaultOAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            User userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika powiązanego z tym kontem Discord"));
            boolean isPasswordSet = userEntity.getPassword() != null && !userEntity.getPassword().isEmpty();
            Map<String, Object> response = CustomReturnables.getOkResponseMap("Sprawdzono, czy hasło jest ustawione dla użytkownika.");
            response.put("isPasswordSet", isPasswordSet);
            return response;
        } else {
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
        }
    }
    public Map<String, Object> getUserActualPhotoPath() {
        User user = (User) getAuthentication().getPrincipal();
        String userPhotoPath = user.getUserPhotoPath();

        if (userPhotoPath == null || userPhotoPath.isEmpty()) {
            throw new ResourceNotFoundException("Użytkownik nie ma ustawionego zdjęcia profilowego.");
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano aktualną ścieżkę zdjęciową użytkownika.");
        response.put("userPhotoPath", userPhotoPath);
        return response;
    }
}
