package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeapon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCustomWeaponDTO {
    private Long baseWeaponId;
    private Long characterId;
    private int dmg;
    private int magazineCapacity;
    private short numberOfAttacks;
    private String handType;
    private boolean isHidden;
    private String quality;
    private String status;
    private String description;
}