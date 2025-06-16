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
    private Integer dmg;
    private Integer magazineCapacity;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    private String quality;
    private Short freeModSlots;
    private String status;
    private String description;
}