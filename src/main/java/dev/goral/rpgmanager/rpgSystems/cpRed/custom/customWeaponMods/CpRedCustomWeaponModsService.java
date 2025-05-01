package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomWeaponModsService {
    private final CpRedCustomWeaponModsRepository cpRedCustomWeaponModsRepository;

//    // Pobierz wszystkie modyfikacje broni
//    public List<CpRedCustomWeaponModsDTO> getAllWeaponMods() {
//
//    }
//
//    // Pobierz modyfikację broni po id
//    public CpRedCustomWeaponModsDTO getWeaponModById(Long weaponModId) {
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
//    public List<CpRedCustomWeaponMods> getAllWeaponModsForAdmin() {
//        return cpRedCustomWeaponModsRepository.findAll();
//    }
}
