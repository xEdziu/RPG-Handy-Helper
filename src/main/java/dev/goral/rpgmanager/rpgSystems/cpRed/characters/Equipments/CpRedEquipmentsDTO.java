package dev.goral.rpgmanager.rpgSystems.cpRed.characters.Equipments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedEquipmentsDTO {
    private String name;
    private int price;
    private String availability;
    private String description;
}