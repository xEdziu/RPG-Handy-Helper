package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedWeaponsService {
    private final CpRedWeaponsRepository cpRedWeaponsRepository;

    // Pobierz wszystkie bronie
    public List<CpRedWeaponsDTO> getAllWeapons() {

    }

    // Pobierz broń po id
    public CpRedWeaponsDTO getWeaponById(Long weaponId) {

    }

    // Pobierz wszystkie bronie dla admina
    public List<CpRedWeapons> getAllWeaponsForAdmin() {
        return cpRedWeaponsRepository.findAll();
    }

    // Dodać broń
    public Map<String, Object> addWeapon(CpRedWeapons cpRedWeapons) {

    }

    // Modyfikować broń
    public Map<String, Object> updateWeapon(Long weaponId, CpRedWeapons cpRedWeapons) {

    }
}
