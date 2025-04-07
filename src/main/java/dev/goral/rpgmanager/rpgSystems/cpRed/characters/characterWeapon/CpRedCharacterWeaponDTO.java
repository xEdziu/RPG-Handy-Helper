package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeapon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterWeaponDTO {
    private Long weaponId;
    private Long characterId;
    private String quality;
    private int ammunition;
    private String status;
    private String description;
}
