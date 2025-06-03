package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.criticalInjuries.CpRedCriticalInjuriesInjuryPlace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CpRedCustomCriticalInjuries {
    @Id
    @SequenceGenerator(
            name="cpRedCustomCriticalInjuries_sequence",
            sequenceName = "cpRedCustomCriticalInjuries_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomCriticalInjuries_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "game_id",
            referencedColumnName = "id"
    )
    private Game gameId;
    @Enumerated(EnumType.STRING)
    CpRedCriticalInjuriesInjuryPlace injuryPlace;
    private String name;
    private String effects;
    private String patching;
    private String treating;
}