package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.stats.CpRedStats;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class CpRedCharacterStats {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterStats_sequence",
            sequenceName = "cpRedCharacterStats_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterStats_sequence"
    )
    private Long id;

    private Integer maxStatLevel;
    private Integer currentStatLevel;

    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters characterId;
    @ManyToOne
    @JoinColumn(
            name = "stat_id",
            referencedColumnName = "id"
    )
    private CpRedStats statId;
}