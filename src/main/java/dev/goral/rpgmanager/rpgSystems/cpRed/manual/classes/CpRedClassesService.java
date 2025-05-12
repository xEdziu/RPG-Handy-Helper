package dev.goral.rpgmanager.rpgSystems.cpRed.manual.classes;

import dev.goral.rpgmanager.rpgSystems.RpgSystemsDTO;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CpRedClassesService {

    private final CpRedClassesRepository cpRedClassesRepository;
    private final UserRepository userRepository;

   public List<CpRedClassesDTO> getAllClasses() {
        return cpRedClassesRepository.findAll()
                .stream()
                .map(cpRedClasses -> new CpRedClassesDTO(
                        cpRedClasses.getName(),
                        cpRedClasses.getDescription()
                ))
                .collect(Collectors.toList());
    }

   public CpRedClassesDTO getClassById(Long classId) {
        CpRedClasses classes= cpRedClassesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Klasa o id " + classId + " nie istnieje"));
        return new CpRedClassesDTO(classes.getName(), classes.getDescription());
    }


    public List<CpRedClasses> getAllClassesForAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals("ROLE_ADMIN")) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }
        return cpRedClassesRepository.findAll();
   }


    public Map<String, Object> addClass(CpRedClasses cpRedClasses) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }

        if(cpRedClasses.getName() == null ||
               cpRedClasses.getDescription() == null){
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }

        String name = cpRedClasses.getName().trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Niewłaściwa nazwa klasy");
        }

        if (name.length() > 255) {
            throw new IllegalArgumentException("Nazwa klasy nie może mieć więcej niż 255 znaków.");
        }
        String description = cpRedClasses.getDescription().trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException("Opis klasy jest wymagany.");
        }

        if (description.length() > 1000) {
            throw new IllegalArgumentException("Opis klasy nie może mieć więcej niż 1000 znaków.");
        }

        if(cpRedClassesRepository.existsByName(name)) {
            throw new IllegalStateException("Klasa o nazwie " + name + " już istnieje");
        }
        cpRedClasses.setName(name);
        cpRedClasses.setDescription(description);
        cpRedClassesRepository.save(cpRedClasses);
        return CustomReturnables.getOkResponseMap("Klasa " + cpRedClasses.getName() + " została dodana");
    }

    public Map<String, Object> updateClass(Long classId, CpRedClasses cpRedClasses) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }
        CpRedClasses existingClass = cpRedClassesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Klasa o id " + classId + " nie istnieje"));
        if(cpRedClasses.getName() == null ||
                cpRedClasses.getDescription() == null){
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }
        String name = cpRedClasses.getName().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Niewłaściwa nazwa klasy");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Nazwa klasy nie może mieć więcej niż 255 znaków.");
        }
        String description = cpRedClasses.getDescription().trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Opis klasy jest wymagany.");
        }
        if (description.length() > 1000) {
            throw new IllegalArgumentException("Opis klasy nie może mieć więcej niż 1000 znaków.");
        }
        if(cpRedClassesRepository.existsByName(name)) {
            throw new IllegalStateException("Klasa o nazwie " + name + " już istnieje");
        }
        existingClass.setName(name);
        existingClass.setDescription(description);
        cpRedClassesRepository.save(existingClass);
        return CustomReturnables.getOkResponseMap("Klasa " + cpRedClasses.getName() + " została zaktualizowana");
    }
}
