package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods;

import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomWeaponModsService {
    private final CpRedCustomWeaponModsRepository cpRedCustomWeaponModsRepository;


    // Pobierz wszystkie modyfikacje broni
    public Map<String, Object> getAllWeaponMods() {
        List<CpRedCustomWeaponModsDTO> allCustomWeaponMods = cpRedCustomWeaponModsRepository.findAll().stream()
                .map(CpRedCustomWeaponMods -> new CpRedCustomWeaponModsDTO(
                        CpRedCustomWeaponMods.getGameId().getId(),
                        CpRedCustomWeaponMods.getName(),
                        CpRedCustomWeaponMods.getPrice(),
                        CpRedCustomWeaponMods.getSize(),
                        CpRedCustomWeaponMods.getAvailability().toString(),
                        CpRedCustomWeaponMods.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe modyfikacje broni zostały pobrane.");
        response.put("customWeaponMods", allCustomWeaponMods);
        return response;
    }
//
//    // Pobierz modyfikację broni po id
//    public Map<String, Object> getWeaponModById(Long weaponModId) {
//
//    }
//
//    // Dodaj modyfikację broni
//    public Map<String, Object> addWeaponMod(CpRedCustomWeaponMods cpRedCustomWeaponMods) {
//
//    }
//
//    // Modyfikować modyfikację broni
//    public Map<String, Object> updateWeaponMod(Long weaponModId, CpRedCustomWeaponMods cpRedCustomWeaponMods) {
//
//    }
//
//    // Pobierz wszystkie modyfikacje broni dla admina
//    public Map<String, Object> getAllWeaponModsForAdmin() {
//
//    }
}
