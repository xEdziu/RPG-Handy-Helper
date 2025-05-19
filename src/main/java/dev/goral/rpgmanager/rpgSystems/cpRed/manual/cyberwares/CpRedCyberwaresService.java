package dev.goral.rpgmanager.rpgSystems.cpRed.manual.cyberwares;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors.CpRedArmorsDTO;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CpRedCyberwaresService {
    private final CpRedCyberwaresRepository cpRedCyberwaresRepository;
    private final UserRepository userRepository;

    // Pobierz wszystkie cyberware
    public Map<String,Object> getAllCyberwares() {
        List<CpRedCyberwaresDTO> cpRedCyberwaresList = cpRedCyberwaresRepository.findAll().stream().
                map(cpRedCyberwares-> new CpRedCyberwaresDTO(
                        cpRedCyberwares.getName(),
                        cpRedCyberwares.getMountPlace().toString(),
                        cpRedCyberwares.getRequirements(),
                        cpRedCyberwares.getHumanityLoss(),
                        cpRedCyberwares.getSize(),
                        cpRedCyberwares.getInstallationPlace().toString(),
                        cpRedCyberwares.getPrice(),
                        cpRedCyberwares.getAvailability().toString(),
                        cpRedCyberwares.getDescription()
                )).toList();
        Map<String, Object> respone = CustomReturnables.getOkResponseMap("Pobrano wszystkie wszczepy.");
        respone.put("cyberwares", cpRedCyberwaresList);
        return respone;

    }
    // Pobierz cyberware po id
    public Map<String,Object> getCyberwareById(Long cyberwareId) {
        CpRedCyberwaresDTO cyberware = cpRedCyberwaresRepository.findById(cyberwareId).map(
                cpRedCyberwares -> new CpRedCyberwaresDTO(
                        cpRedCyberwares.getName(),
                        cpRedCyberwares.getMountPlace().toString(),
                        cpRedCyberwares.getRequirements(),
                        cpRedCyberwares.getHumanityLoss(),
                        cpRedCyberwares.getSize(),
                        cpRedCyberwares.getInstallationPlace().toString(),
                        cpRedCyberwares.getPrice(),
                        cpRedCyberwares.getAvailability().toString(),
                        cpRedCyberwares.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Wszczep o id " + cyberwareId + " nie istnieje"));
        Map<String, Object> respone = CustomReturnables.getOkResponseMap("Pobrano wszczep o id " + cyberwareId);
        respone.put("cyberware", cyberware);
        return respone;
    }
    // Pobierz wszystkie cyberware dla admina
    public Map<String,Object> getAllCyberwaresForAdmin() {
        List<CpRedCyberwares> allCyberwaresList = cpRedCyberwaresRepository.findAll();
        Map<String, Object> respone = CustomReturnables.getOkResponseMap("Pobrano wszystkie wszczepy.");
        respone.put("cyberwares", allCyberwaresList);
        return respone;
    }

    // Dodać cyberware
    public Map<String, Object> addCyberware(CpRedCyberwares cpRedCyberwares) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania wszczepów.");
        }
        if(cpRedCyberwares.getName()==null ||
        cpRedCyberwares.getMountPlace().toString()==null ||
                cpRedCyberwares.getRequirements()==null ||
                cpRedCyberwares.getHumanityLoss()==null ||
                cpRedCyberwares.getInstallationPlace().toString()==null ||
                cpRedCyberwares.getAvailability().toString()==null ||
                cpRedCyberwares.getDescription()==null) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }
        if (cpRedCyberwares.getSize() <= 0) {
            throw new IllegalStateException("Rozmiar nie może być ujemny lub równy zero.");
        }
        if(cpRedCyberwaresRepository.existsByName(cpRedCyberwares.getName())) {
            throw new IllegalStateException("Wszczep o tej nazwie już istnieje.");
        }
        if (cpRedCyberwares.getName().isEmpty() || cpRedCyberwares.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa wszczepu nie może być pusta.");
        }
        if (cpRedCyberwares.getName().length() > 255) {
            throw new IllegalStateException("Nazwa wszczepu nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCyberwares.getPrice() < 0) {
            throw new IllegalStateException("Cena nie może być ujemna.");
        }
        if (cpRedCyberwares.getDescription().isEmpty() || cpRedCyberwares.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis wszczepu nie może być pusty.");
        }
        if (cpRedCyberwares.getDescription().length() > 500) {
            throw new IllegalStateException("Opis wszczepu nie może być dłuższy niż 500 znaków.");
        }
        CpRedCyberwares newCyberware = new CpRedCyberwares(
                null,
                cpRedCyberwares.getName(),
                cpRedCyberwares.getMountPlace(),
                cpRedCyberwares.getRequirements(),
                cpRedCyberwares.getHumanityLoss(),
                cpRedCyberwares.getSize(),
                cpRedCyberwares.getInstallationPlace(),
                cpRedCyberwares.getPrice(),
                cpRedCyberwares.getAvailability(),
                cpRedCyberwares.getDescription()
        );
        cpRedCyberwaresRepository.save(newCyberware);
        return CustomReturnables.getOkResponseMap("Wszczep został dodany.");
    }

    // Modyfikować cyberware
    public Map<String, Object> updateCyberware(Long cyberwareId, CpRedCyberwares cpRedCyberwares) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania wszczepów.");
        }
        CpRedCyberwares cyberwareToUpdate = cpRedCyberwaresRepository.findById(cyberwareId)
                .orElseThrow(() -> new ResourceNotFoundException("Wszczep o id " + cyberwareId + " nie istnieje"));

        if (cpRedCyberwares.getName() != null) {
            if (cpRedCyberwaresRepository.existsByName(cpRedCyberwares.getName())) {
                throw new IllegalStateException("Wszczep o tej nazwie już istnieje.");
            }
            if (cpRedCyberwares.getName().isEmpty() || cpRedCyberwares.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa wszczepu nie może być pusta.");
            }
            if (cpRedCyberwares.getName().length() > 255) {
                throw new IllegalStateException("Nazwa wszczepu nie może być dłuższa niż 255 znaków.");
            }
            cyberwareToUpdate.setName(cpRedCyberwares.getName());
        }

        if (cpRedCyberwares.getMountPlace() != null) {
            cyberwareToUpdate.setMountPlace(cpRedCyberwares.getMountPlace());
        }
        if (cpRedCyberwares.getRequirements() != null) {
            cyberwareToUpdate.setRequirements(cpRedCyberwares.getRequirements());
        }
        if (cpRedCyberwares.getHumanityLoss() != null) {
            cyberwareToUpdate.setHumanityLoss(cpRedCyberwares.getHumanityLoss());
        }
        if (cpRedCyberwares.getSize() <= 0) {
            throw new IllegalStateException("Rozmiar nie może być ujemny lub równy zero.");
        }
        cyberwareToUpdate.setSize(cpRedCyberwares.getSize());

        if (cpRedCyberwares.getInstallationPlace() != null) {
            cyberwareToUpdate.setInstallationPlace(cpRedCyberwares.getInstallationPlace());
        }
        if (cpRedCyberwares.getPrice() < 0) {
            throw new IllegalStateException("Cena nie może być ujemna.");
        }
        cyberwareToUpdate.setPrice(cpRedCyberwares.getPrice());

        if (cpRedCyberwares.getAvailability() != null) {
            cyberwareToUpdate.setAvailability(cpRedCyberwares.getAvailability());
        }
        if (cpRedCyberwares.getDescription() != null) {
            if (cpRedCyberwares.getDescription().isEmpty() || cpRedCyberwares.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis wszczepu nie może być pusty.");
            }
            if (cpRedCyberwares.getDescription().length() > 500) {
                throw new IllegalStateException("Opis wszczepu nie może być dłuższy niż 500 znaków.");
            }
            cyberwareToUpdate.setDescription(cpRedCyberwares.getDescription());
        }
        cpRedCyberwaresRepository.save(cyberwareToUpdate);
        return CustomReturnables.getOkResponseMap("Wszczep został zaktualizowany.");
    }
}