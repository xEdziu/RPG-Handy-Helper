package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedWeaponsDTO {
    private String requireSkillId;
    private String type;
    private int damage;
    private int magazineCapacity;
    private String ammunition;
    private short la;
    private short hands;
    private boolean isHidden;
    private int price;
    private String availability;
    private String description;
}
