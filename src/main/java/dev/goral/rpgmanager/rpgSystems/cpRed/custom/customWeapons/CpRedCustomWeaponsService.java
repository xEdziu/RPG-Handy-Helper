package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomWeaponsService {
    private final CpRedCustomWeaponsRepository cpRedCustomWeaponsRepository;

    // Pobierz wszystkie customowe bronie
    public List<CpRedCustomWeaponsDTO> getAllCustomWeapons() {

    }

    // Pobierz customową broń po id
    public CpRedCustomWeaponsDTO getCustomWeaponById(Long customWeaponId) {

    }

    // Dodaj customową broń
    public Map<String, Object> addCustomWeapon(CpRedCustomWeapons cpRedCustomWeapons) {

    }

    // Modyfikuj customową broń
    public Map<String, Object> updateCustomWeapon(Long customWeaponId, CpRedCustomWeapons cpRedCustomWeapons) {

    }

    // Pobierz wszystkie customowe bronie dla admina
    public List<CpRedCustomWeapons> getAllCustomWeaponsForAdmin() {
        return cpRedCustomWeaponsRepository.findAll();
    }


}
