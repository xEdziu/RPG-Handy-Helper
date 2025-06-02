package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.classes;

import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import dev.goral.rpghandyhelper.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedClassesService {

    private final CpRedClassesRepository cpRedClassesRepository;
    private final UserRepository userRepository;

   public Map<String, Object> getAllClasses() {
       List<CpRedClasses> classes= cpRedClassesRepository.findAll();
       List<CpRedClassesDTO> classesDTO = classes.stream()
               .map(cpRedClasses -> new CpRedClassesDTO(
                       cpRedClasses.getName(),
                       cpRedClasses.getDescription()
               )).toList();
        if(classesDTO.isEmpty()){
            return CustomReturnables.getOkResponseMap("Brak klas");
        }
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano klasy");
        response.put("classes", classesDTO);
        return response;
    }

   public Map<String, Object> getClassById(Long classId) {
        CpRedClassesDTO classes = cpRedClassesRepository.findById(classId)
                .map(cpRedClasses -> new CpRedClassesDTO(
                        cpRedClasses.getName(),
                        cpRedClasses.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Klasa o id " + classId + " nie istnieje"));
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano klasę");
        response.put("class", classes);
        return response;
    }


    public Map<String, Object> getAllClassesForAdmin() {
       List<CpRedClasses> allClasses = cpRedClassesRepository.findAll();
       Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano klasy");
       response.put("classes", allClasses);
         return response;
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
            throw new IllegalStateException("Nie masz uprawnień do modyfikowania klas.");
        }
        CpRedClasses classToUpdate = cpRedClassesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Klasa o id " + classId + " nie istnieje"));

        if (cpRedClasses.getName() != null) {
            if (cpRedClassesRepository.existsByName(cpRedClasses.getName())) {
                throw new IllegalStateException("Klasa o tej nazwie już istnieje.");
            }
            if (cpRedClasses.getName().isEmpty() || cpRedClasses.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa klasy nie może być pusta.");
            }
            if (cpRedClasses.getName().length() > 255) {
                throw new IllegalStateException("Nazwa klasy nie może być dłuższa niż 255 znaków.");
            }
            classToUpdate.setName(cpRedClasses.getName());
        }

        if (cpRedClasses.getDescription() != null) {
            if (cpRedClasses.getDescription().isEmpty() || cpRedClasses.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis klasy nie może być pusty.");
            }
            if (cpRedClasses.getDescription().length() > 500) {
                throw new IllegalStateException("Opis klasy nie może być dłuższy niż 500 znaków.");
            }
            classToUpdate.setDescription(cpRedClasses.getDescription());
        }

        cpRedClassesRepository.save(classToUpdate);
        return CustomReturnables.getOkResponseMap("Klasa została zaktualizowana");
    }
}