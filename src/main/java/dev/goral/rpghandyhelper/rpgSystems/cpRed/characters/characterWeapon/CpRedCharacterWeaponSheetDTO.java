package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition.CpRedCharacterAmmunitionSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod.CpRedCharacterWeaponModSheetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterWeaponSheetDTO {
    private Long characterWeaponId;
    private Long baseWeaponId;
    private Boolean isBaseWeaponCustom;
    private String weaponName;
    private String weaponDmg; // Dmg + "k6"
    private Integer magazineCapacity;
    private List<CpRedCharacterAmmunitionSheetDTO> ammoList; // Lista wszystkich pasujących amunicji
    private Short numberOfAttacks;
    private List<CpRedCharacterWeaponModSheetDTO> modList; // Lista modów broni
    private String description;
}
