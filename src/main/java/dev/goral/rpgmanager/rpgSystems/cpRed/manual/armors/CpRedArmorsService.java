package dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.classes.CpRedClasses;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.classes.CpRedClassesDTO;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedArmorsService {
    private final CpRedArmorsRepository cpRedArmorsRepository;
    private final UserRepository userRepository;

    // Pobierz wszystkie pancerze
    public Map<String,Object> getAllArmors() {
        List<CpRedArmors> armors= cpRedArmorsRepository.findAll();
        List<CpRedArmorsDTO> armorsDTO = armors.stream().map(armor ->
                new CpRedArmorsDTO(
                        armor.getType().toString(),
                        armor.getArmorPoints(),
                        armor.getPenalty(),
                        armor.getPrice(),
                        armor.getAvailability().toString(),
                        armor.getDescription()
                )).toList();
        if(armorsDTO.isEmpty()){
            return CustomReturnables.getOkResponseMap("Brak pancerzy");
        }
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano pancerze");
        response.put("classes", armorsDTO);
        return response;
    }

    public Map<String,Object> getArmorById(Long armorId) {
        CpRedArmorsDTO armor=cpRedArmorsRepository.findById(armorId).
                map(cpRedArmors -> new CpRedArmorsDTO(
                        cpRedArmors.getType().toString(),
                        cpRedArmors.getArmorPoints(),
                        cpRedArmors.getPenalty(),
                        cpRedArmors.getPrice(),
                        cpRedArmors.getAvailability().toString(),
                        cpRedArmors.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Pancerz o id " + armorId + " nie został znaleziony"));
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano pancerz");
        response.put("armor", armor);
        return response;
    }

    public Map<String,Object> getAllArmorsForAdmin() {
        List<CpRedArmors> allArmors = cpRedArmorsRepository.findAll();
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano pancerze");
        response.put("armors", allArmors);
        return response;
    }

    public Map<String, Object> addArmor(CpRedArmors armor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }
        if(armor.getType() == null ||
                armor.getArmorPoints() == 0 ||
                armor.getPrice() == 0 ||
                armor.getAvailability() == null ||
                armor.getDescription() == null) {
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }
        if(armor.getArmorPoints() < 0 ||
                armor.getPenalty() < 0 ||
                armor.getPrice() < 0) {
            throw new IllegalStateException("Parametry nie mogą być ujemne");
        }
        String description = armor.getDescription();
        if(description.length() > 1000) {
            throw new IllegalStateException("Opis nie może być dłuższy niż 1000 znaków");
        }
        CpRedArmors newArmor = new CpRedArmors();
        newArmor.setType(armor.getType());
        newArmor.setArmorPoints(armor.getArmorPoints());
        newArmor.setPenalty(armor.getPenalty());
        newArmor.setPrice(armor.getPrice());
        newArmor.setAvailability(armor.getAvailability());
        newArmor.setDescription(armor.getDescription());
        cpRedArmorsRepository.save(newArmor);
        return CustomReturnables.getOkResponseMap("Pancerz został dodany");
    }
//    // Modyfikować pancerz
    public Map<String, Object> updateArmor(Long armorId, CpRedArmors armor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }
        if(armor.getType() == null ||
                armor.getArmorPoints() == 0 ||
                armor.getPrice() == 0 ||
                armor.getAvailability() == null ||
                armor.getDescription() == null) {
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }
        if(armor.getArmorPoints() < 0 ||
                armor.getPenalty() < 0 ||
                armor.getPrice() < 0) {
            throw new IllegalStateException("Parametry nie mogą być ujemne");
        }
        String description = armor.getDescription();
        if(description.length() > 1000) {
            throw new IllegalStateException("Opis nie może być dłuższy niż 1000 znaków");
        }
        CpRedArmors armorToUpdate = cpRedArmorsRepository.findById(armorId)
                .orElseThrow(() -> new IllegalStateException("Armor with id " + armorId + " does not exist"));
        armorToUpdate.setType(armor.getType());
        armorToUpdate.setArmorPoints(armor.getArmorPoints());
        armorToUpdate.setPenalty(armor.getPenalty());
        armorToUpdate.setPrice(armor.getPrice());
        armorToUpdate.setAvailability(armor.getAvailability());
        armorToUpdate.setDescription(armor.getDescription());
        cpRedArmorsRepository.save(armorToUpdate);
        return CustomReturnables.getOkResponseMap("Pancerz został zaktualizowany");
    }
}
