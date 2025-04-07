package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customArmors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCustomArmorsDTO {
    private Long gameId;
    private String type;
    private int ob;
    private int penalty;
    private int price;
    private String availability;
    private String description;
}