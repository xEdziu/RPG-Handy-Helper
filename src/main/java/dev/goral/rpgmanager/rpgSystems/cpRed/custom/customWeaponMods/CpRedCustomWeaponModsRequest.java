package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods;

import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsAvailability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCustomWeaponModsRequest {
    private Long gameId;
    private String name;
    private int price=-1;
    private int size=1;
    private CpRedItemsAvailability availability;
    private String description;
}
