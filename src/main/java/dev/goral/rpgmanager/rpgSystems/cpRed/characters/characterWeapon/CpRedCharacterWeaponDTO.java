package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeapon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterWeaponDTO {
    private long weaponId;
    private long characterId;
    private String quality;
    private int ammunition;
    private String status;
    private String description;
}
