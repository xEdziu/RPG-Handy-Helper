package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedAmmunitionDTO {
    private String name;
    private String description;
    private int pricePerBullet;
    private String availability;
}