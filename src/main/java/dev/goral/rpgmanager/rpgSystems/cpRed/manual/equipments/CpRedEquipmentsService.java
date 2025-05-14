package dev.goral.rpgmanager.rpgSystems.cpRed.manual.equipments;

import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedEquipmentsService {
    private final CpRedEquipmentsRepository cpRedEquipmentsRepository;
    private final UserRepository userRepository;

    // Pobierz wszystkie przedmioty
    public Map<String, Object>  getAllEquipments() {
        List<CpRedEquipmentsDTO> cpRedEquipmentsList = cpRedEquipmentsRepository.findAll().stream().
                map(
                cpRedEquipments -> new CpRedEquipmentsDTO(
                        cpRedEquipments.getName(),
                        cpRedEquipments.getPrice(),
                        cpRedEquipments.getAvailability().toString(),
                        cpRedEquipments.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wyposażenie zostało pobrane.");
        response.put("equipments", cpRedEquipmentsList);
        return response;
    }

    // Pobierz przedmiot po id
    public Map<String, Object> getEquipmentById(Long equipmentId) {
        CpRedEquipmentsDTO cpRedEquipments = cpRedEquipmentsRepository.findById(equipmentId)
                .map(
                        equipments -> new CpRedEquipmentsDTO(
                                equipments.getName(),
                                equipments.getPrice(),
                                equipments.getAvailability().toString(),
                                equipments.getDescription()
                        )).orElseThrow(() -> new RuntimeException("Przedmiot o id " + equipmentId + "nie istnieje"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano przedmiot.");
        response.put("equipment", cpRedEquipments);
        return response;

    }

    // Pobierz wszystkie przedmioty dla admina
    public Map<String, Object> getAllEquipmentsForAdmin() {
        List<CpRedEquipments> allEquipments = cpRedEquipmentsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("PWyposażenie zostało pobrane.");
        response.put("equipments", allEquipments);
        return response;

    }

    // Dodaj przedmiot
    public Map<String, Object> addEquipment(CpRedEquipments cpRedEquipments) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }
        if (cpRedEquipments.getName() == null ||
                cpRedEquipments.getAvailability() == null ||
                cpRedEquipments.getDescription() == null) {
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }
        if(cpRedEquipments.getPrice() <= 0) {
            throw new IllegalStateException("Cena nie może być ujemna lub równa zero");
        }
        String name = cpRedEquipments.getName().trim();
        if (name.isEmpty()) {
            throw new IllegalStateException("Nie podano nazwy przedmiotu");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Nazwa klasy nie może mieć więcej niż 255 znaków.");
        }
        String description = cpRedEquipments.getDescription().trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Opis klasy jest wymagany.");
        }
        if (description.length() > 1000) {
            throw new IllegalArgumentException("Opis klasy nie może mieć więcej niż 1000 znaków.");
        }
        if(cpRedEquipmentsRepository.existsByName(name)) {
            throw new IllegalStateException("Klasa o nazwie " + name + " już istnieje");
        }
        cpRedEquipments.setName(name);
        cpRedEquipments.setDescription(description);
        cpRedEquipments.setPrice(cpRedEquipments.getPrice());
        cpRedEquipments.setAvailability(cpRedEquipments.getAvailability());
        cpRedEquipmentsRepository.save(cpRedEquipments);
        return CustomReturnables.getOkResponseMap("Przedmiot " + cpRedEquipments.getName() + " został dodany");
    }

    // Modyfikuj przedmiot
    public Map<String, Object> updateEquipment(Long equipmentId, CpRedEquipments cpRedEquipments) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania wszczepów.");
        }
        CpRedEquipments eqToUpdate = cpRedEquipmentsRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Przedmiot o id " + equipmentId + " nie istnieje"));

        if (cpRedEquipments.getName() != null) {
            if (cpRedEquipmentsRepository.existsByName(cpRedEquipments.getName())) {
                throw new IllegalStateException("Przedmiot o tej nazwie już istnieje.");
            }
            if (cpRedEquipments.getName().isEmpty() || cpRedEquipments.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa przedmiot nie może być pusta.");
            }
            if (cpRedEquipments.getName().length() > 255) {
                throw new IllegalStateException("Nazwa przedmiot nie może być dłuższa niż 255 znaków.");
            }
            eqToUpdate.setName(cpRedEquipments.getName());
        }

        if (cpRedEquipments.getPrice() < 0) {
            throw new IllegalStateException("Cena nie może być ujemna.");
        }
        eqToUpdate.setPrice(cpRedEquipments.getPrice());

        if (cpRedEquipments.getAvailability() != null) {
            eqToUpdate.setAvailability(cpRedEquipments.getAvailability());
        }

        if (cpRedEquipments.getDescription() != null) {
            if (cpRedEquipments.getDescription().isEmpty() || cpRedEquipments.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis przedmiotu nie może być pusty.");
            }
            if (cpRedEquipments.getDescription().length() > 500) {
                throw new IllegalStateException("Opis przedmiotu nie może być dłuższy niż 500 znaków.");
            }
            eqToUpdate.setDescription(cpRedEquipments.getDescription());
        }
        cpRedEquipmentsRepository.save(eqToUpdate);
        return CustomReturnables.getOkResponseMap("Przedmiot został zaktualizowany.");
    }
}
