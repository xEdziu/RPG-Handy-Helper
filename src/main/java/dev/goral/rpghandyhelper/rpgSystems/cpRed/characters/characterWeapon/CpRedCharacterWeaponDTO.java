package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterWeaponDTO {
    private Long id;
    private Long baseWeaponId;
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
