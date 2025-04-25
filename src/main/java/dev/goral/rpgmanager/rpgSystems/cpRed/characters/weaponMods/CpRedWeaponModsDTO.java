package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weaponMods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedWeaponModsDTO {
    private String name;
    private int price;
    private int size;
    private String availability;
    private String description;

}
