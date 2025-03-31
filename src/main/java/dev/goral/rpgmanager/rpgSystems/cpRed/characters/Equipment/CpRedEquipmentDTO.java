package dev.goral.rpgmanager.rpgSystems.cpRed.characters.Equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedEquipmentDTO {
    private String name;
    private int price;
    private String availability;
    private String description;
}