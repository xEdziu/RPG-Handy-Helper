package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterSkills;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills.CpRedSkills;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CpRedCharacterSkills {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterSkills_sequence",
            sequenceName = "cpRedCharacterSkills_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterSkills_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters characterId;

    @ManyToOne
    @JoinColumn(
            name = "skill_id",
            referencedColumnName = "id"
    )
    private CpRedSkills skillId;

    private int skillLevel=0;
}
