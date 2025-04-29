package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weaponMods;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedWeaponModsService {
    private final CpRedWeaponModsRepository cpRedWeaponModsRepository;

    // Pobierz wszystkie modyfikacje broni
    public List<CpRedWeaponModsDTO> getAllWeaponMods() {

    }

    // Pobierz modyfikacje broni po id
    public CpRedWeaponModsDTO getWeaponModById(Long weaponModId) {

    }

    // Pobierz wszystkie modyfikacje broni dla admina
    public List<CpRedWeaponMods> getAllWeaponModsForAdmin() {
        return cpRedWeaponModsRepository.findAll();
    }

    // Dodaj modyfikacje broni
    public Map<String, Object> addWeaponMod(CpRedWeaponMods cpRedWeaponMods) {

    }

    // Modyfikuj modyfikacje broni
    public Map<String, Object> updateWeaponMod(Long weaponModId, CpRedWeaponMods cpRedWeaponMods) {

    }
}
