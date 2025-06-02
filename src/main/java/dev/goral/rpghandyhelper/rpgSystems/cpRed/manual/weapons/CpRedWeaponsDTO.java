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
    private int damage;
    private int magazineCapacity;
    private Long standardAmmunitionId;
    private short numberOfAttacks;
    private short handType;
    private boolean isHidden;
    private String quality;
    private int price;
    private String availability;
    private boolean isModifiable;
    private String description;
}
