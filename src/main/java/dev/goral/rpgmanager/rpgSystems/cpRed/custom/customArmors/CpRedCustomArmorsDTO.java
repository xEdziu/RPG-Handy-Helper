package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCustomArmorsDTO {
    private Long gameId;
    private String name;
    private String type;
    private int armorPoints;
    private int penalty;
    private int price;
    private String availability;
    private String description;
}