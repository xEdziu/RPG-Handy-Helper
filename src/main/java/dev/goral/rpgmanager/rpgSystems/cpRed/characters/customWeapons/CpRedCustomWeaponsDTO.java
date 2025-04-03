package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeapons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCustomWeaponsDTO {
    private long id;
    private long gameId;
    private long requiredSkillId;
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