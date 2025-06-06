package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.criticalInjuries.CpRedCriticalInjuries;
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
public class CpRedCharacterCriticalInjuries {
    @Id
    @SequenceGenerator(
            name="cpRedCharacterCriticalInjuries_sequence",
            sequenceName = "cpRedCharacterCriticalInjuries_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCriticalInjuries_sequence"
    )
    private Long id;
    @Enumerated(EnumType.STRING)
    CpRedCharacterCriticalInjuriesStatus status;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters characterId;
    @ManyToOne
    @JoinColumn(
            name = "injuries_id",
            referencedColumnName = "id"
    )
    private CpRedCriticalInjuries injuriesId;
}