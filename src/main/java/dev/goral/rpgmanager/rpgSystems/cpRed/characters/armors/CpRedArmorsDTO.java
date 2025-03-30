package dev.goral.rpgmanager.rpgSystems.cpRed.characters.armors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedArmorsDTO {
    private String type;
    private int ob;
    private int penalty;
    private int price;
    private String availability;
    private String description;
}