package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomAmmunitionRequest {
    private Long gameId;
    private String name;
    private String description;
    private int pricePerBullet = -1;
    private CpRedItemsAvailability availability;
}
