package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedWeaponsDTO {
    private long requiredSkillId;
    private String type;
    private int damage;
    private int magazineCapacity;
    private long standardAmmunitionId;
    private short numberOfAttacks;
    private short handType;
    private boolean isHidden;
    private String quality;
    private int price;
    private String availability;
    private boolean isModifiable;
    private String description;
}
