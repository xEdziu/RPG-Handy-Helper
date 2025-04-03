package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeapon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCustomWeaponDTO {
    private long weaponId;
    private long characterId;
    private String quality;
    private int ammunition;
    private String status;
    private String description;
}