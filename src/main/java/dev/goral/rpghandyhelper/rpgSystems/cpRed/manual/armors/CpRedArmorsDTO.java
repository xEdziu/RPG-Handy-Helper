package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.armors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedArmorsDTO {
    private String name;
    private int armorPoints;
    private int penalty;
    private int price;
    private String availability;
    private String description;
}