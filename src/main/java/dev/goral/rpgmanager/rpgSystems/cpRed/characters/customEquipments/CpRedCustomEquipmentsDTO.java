package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customEquipments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCustomEquipmentsDTO {
    private Long gameId;
    private String name;
    private int price;
    private String availability;
    private String description;
}