package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedWeaponsDTO {
    private Long requiredSkillId;
    private String type;
    private Integer damage;
    private Integer magazineCapacity;
    private Long standardAmmunitionId;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    private String quality;
    private Integer price;
    private String availability;
    private Boolean isModifiable;
    private Short modSlots;
    private String description;
}
