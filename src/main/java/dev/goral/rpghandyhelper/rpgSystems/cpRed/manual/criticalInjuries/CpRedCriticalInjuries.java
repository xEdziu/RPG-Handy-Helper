package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.criticalInjuries;

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
public class CpRedCriticalInjuries {
    @Id
    @SequenceGenerator(
            name="cpRedCriticalinjuries_sequence",
            sequenceName = "cpRedCriticalinjuries_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCriticalinjuries_sequence"
    )
    private Long id;
    private int rollValue=-1;
    @Enumerated(EnumType.STRING)
    private CpRedCriticalInjuriesInjuryPlace injuryPlace;
    private String name;
    private String effects;
    private String patching;
    private String treating;
}