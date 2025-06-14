package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCustomWeaponDTO {
    private Long id;
    private Long baseCustomWeaponId;
    private Long characterId;
    private int dmg;
    private int magazineCapacity;
    private short numberOfAttacks;
    private Short handType;
    private boolean isHidden;
    private String quality;
    private String status;
    private String description;
}