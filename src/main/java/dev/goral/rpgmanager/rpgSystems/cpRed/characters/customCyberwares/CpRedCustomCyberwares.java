package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customCyberwares;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.cyberwares.CpRedCyberwares;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.cyberwares.CpRedCyberwaresInstallationPlace;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.cyberwares.CpRedCyberwaresMountPlace;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.item.CpRedItemAvailability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CpRedCustomCyberwares {
    @Id
    @SequenceGenerator(
            name = "customCyberware_sequence",
            sequenceName = "customCyberware_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customCyberware_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "game_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Game gameId;
    private String name;
    @Enumerated(EnumType.STRING)
    private CpRedCyberwaresMountPlace mountPlace;
    private String requirements;
    private String uc;
    @Enumerated(EnumType.STRING)
    private CpRedCyberwaresInstallationPlace installationPlace;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemAvailability availability;
    private String description;
}