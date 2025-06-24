package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition.CpRedCharacterAmmunitionSheetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterWeaponSheetDTO {
    private String weaponName;
    private String weaponDmg; // Dmg + "k6"
    private Integer magazineCapacity;
    private List<CpRedCharacterAmmunitionSheetDTO> ammoList; // Lista wszystkich pasujących amunicji
    private Short numberOfAttacks;
    private List<> modList; // Lista modów broni
    private String description;
}
