package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customEquipments;

import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsAvailability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCustomEquipmentsRequest {
    private Long gameId;
    private String name;
    private int price = -1;
    private CpRedItemsAvailability availability;
    private String description;
}
