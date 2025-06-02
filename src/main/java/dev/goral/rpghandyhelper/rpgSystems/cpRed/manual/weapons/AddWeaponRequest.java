package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsQuality;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddWeaponRequest {
    private Long requiredSkillId;
    private String name;
    private int damage = -1; //quantity of d6
    private int magazineCapacity = -1;
    private Long standardAmmunitionId;
    private short numberOfAttacks = -1;
    private short handType = -1;
    private boolean isHidden;
    private CpRedItemsQuality quality;
    private int price = -1;
    private CpRedItemsAvailability availability;
    private boolean isModifiable;

    @Column(length = 500)
    private String description;
}
