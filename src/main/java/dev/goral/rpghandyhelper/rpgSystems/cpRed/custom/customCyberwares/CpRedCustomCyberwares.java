package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwaresInstallationPlace;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwaresMountPlace;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CpRedCustomCyberwares {
    @Id
    @SequenceGenerator(
            name = "cpRedCustomCyberwares_sequence",
            sequenceName = "cpRedCustomCyberwares_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomCyberwares_sequence"
    )
    private Long id;
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
    private String humanityLoss;
    private int size;
    @Enumerated(EnumType.STRING)
    private CpRedCyberwaresInstallationPlace installationPlace;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}