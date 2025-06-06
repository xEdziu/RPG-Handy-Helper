package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCyberwares {
    @Id
    @SequenceGenerator(
        name = "cpRedCyberwares_sequence",
        sequenceName = "cpRedCyberwares_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCyberwares_sequence"
    )
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CpRedCyberwaresMountPlace mountPlace;
    private String requirements;
    private String humanityLoss;
    private int size=-1;
    @Enumerated(EnumType.STRING)
    private CpRedCyberwaresInstallationPlace installationPlace;
    private int price=-1;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}