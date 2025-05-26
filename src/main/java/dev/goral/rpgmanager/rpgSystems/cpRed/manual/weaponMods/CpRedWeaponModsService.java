package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weaponMods;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors.CpRedArmors;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors.CpRedArmorsDTO;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CpRedWeaponModsService {
    private final CpRedWeaponModsRepository cpRedWeaponModsRepository;

    // Pobierz wszystkie modyfikacje broni
    public Map<String, Object> getAllWeaponMods() {
        List<CpRedWeaponMods> mods= cpRedWeaponModsRepository.findAll();
        List<CpRedWeaponModsDTO> modsDTO = mods.stream().map( mod->
                new CpRedWeaponModsDTO(
                        mod.getName(),
                        mod.getPrice(),
                        mod.getSize(),
                        mod.getAvailability().toString(),
                        mod.getDescription()

                )).toList();
        if(modsDTO.isEmpty()){
            return CustomReturnables.getOkResponseMap("Brak modyfikacji broni");
        }
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano modyfikacje broni");
        response.put("weaponMods", modsDTO);
        return response;
    }

    // Pobierz modyfikacje broni po id
    public Map<String, Object> getWeaponModById(Long weaponModId) {
        CpRedWeaponModsDTO weaponMod = cpRedWeaponModsRepository.findById(weaponModId)
                .map(cpRedWeaponMods -> new CpRedWeaponModsDTO(
                        cpRedWeaponMods.getName(),
                        cpRedWeaponMods.getPrice(),
                        cpRedWeaponMods.getSize(),
                        cpRedWeaponMods.getAvailability().toString(),
                        cpRedWeaponMods.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Modyfikacja broni o id " + weaponModId + " nie została znaleziona"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano modyfikację broni");
        response.put("weaponMod", weaponMod);
        return response;
    }
//
//    // Pobierz wszystkie modyfikacje broni dla admina
//    public Map<String, Object> getAllWeaponModsForAdmin() {
//        return cpRedWeaponModsRepository.findAll();
//    }
//
//    // Dodaj modyfikacje broni
//    public Map<String, Object> addWeaponMod(CpRedWeaponMods cpRedWeaponMods) {
//
//    }
//
//    // Modyfikuj modyfikacje broni
//    public Map<String, Object> updateWeaponMod(Long weaponModId, CpRedWeaponMods cpRedWeaponMods) {
//
//    }
}
