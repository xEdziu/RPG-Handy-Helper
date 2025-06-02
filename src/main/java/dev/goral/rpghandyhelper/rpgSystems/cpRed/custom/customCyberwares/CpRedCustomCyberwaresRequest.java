package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwaresInstallationPlace;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwaresMountPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCustomCyberwaresRequest {
    private Long gameId;
    private String name;
    private CpRedCyberwaresMountPlace mountPlace;
    private String requirements;
    private String humanityLoss;
    private int size=-1;
    private CpRedCyberwaresInstallationPlace installationPlace;
    private int price=-1;
    private CpRedItemsAvailability availability;
    private String description;
}
