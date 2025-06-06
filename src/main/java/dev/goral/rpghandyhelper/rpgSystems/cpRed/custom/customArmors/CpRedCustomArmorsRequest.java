package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.armors.CpRedArmorsType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCustomArmorsRequest {
    private Long gameId;
    private String name;
    private CpRedArmorsType type;
    private int armorPoints=-1;
    private int penalty=-1;
    private int price=-1;
    private CpRedItemsAvailability availability;
    private String description;

}
