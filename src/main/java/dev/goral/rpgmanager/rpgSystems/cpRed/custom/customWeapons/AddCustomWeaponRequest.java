package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons;

import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsQuality;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomWeaponRequest {
    private Long gameId = -1L;
    private Long requiredSkillId = -1L;
    private String name;
    private String type; //enum from weapons
    private int damage = -1; //quantity of d6
    private int magazineCapacity = -1;
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
