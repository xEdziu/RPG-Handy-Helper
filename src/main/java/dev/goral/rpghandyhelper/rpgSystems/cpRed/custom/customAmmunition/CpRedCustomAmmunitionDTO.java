package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCustomAmmunitionDTO {
    private Long gameId;
    private String name;
    private String description;
    private int pricePerBullet;
    private String availability;
}