package dev.goral.rpgmanager.rpgSystems.cpRed.characters.Skills;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.stats.CpRedStats;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CpRedSkills {
    @Id
    @SequenceGenerator(
            name = "cpRedSkills_sequence",
            sequenceName = "cpRedSkills_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedSkills_sequence"
    )
    private Long id;
    @Enumerated(EnumType.STRING)
    private CpRedSkillsCategory category;
    private String name;
    @ManyToOne
    @JoinColumn(
            name = "stat_id",
            referencedColumnName = "id"
    )
    private CpRedStats connectedStatId;
    private String description;
}