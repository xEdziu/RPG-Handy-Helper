package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCustomWeaponsDTO {
    private Long id;
    private Long gameId;
    private String name;
    private Long requiredSkillId;
    private String type;
    private int damage;
    private int magazineCapacity;
    private short numberOfAttacks;
    private short handType;
    private boolean isHidden;
    private String quality;
    private int price;
    private String availability;
    private boolean isModifiable;
    private String description;
}