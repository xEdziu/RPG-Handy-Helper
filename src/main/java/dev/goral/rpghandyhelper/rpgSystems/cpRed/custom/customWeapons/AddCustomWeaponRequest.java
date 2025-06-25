package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons;

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
public class AddCustomWeaponRequest {
    private Long gameId;
    private Long requiredSkillId;
    private String name;
    private String type; //enum from weapons
    private Integer damage ; //quantity of d6
    private Integer magazineCapacity;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    private CpRedItemsQuality quality;
    private Integer price;
    private CpRedItemsAvailability availability;
    private Boolean isModifiable;
    private Short modSlots;

    @Column(length = 500)
    private String description;
}
