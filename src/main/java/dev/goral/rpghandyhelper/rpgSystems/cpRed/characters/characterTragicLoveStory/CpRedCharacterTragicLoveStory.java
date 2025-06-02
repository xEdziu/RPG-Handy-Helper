package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterTragicLoveStory;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
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
public class CpRedCharacterTragicLoveStory {
    @Id
    @SequenceGenerator(
            name="cpRedCharacterTragicLoveStory_sequence",
            sequenceName = "cpRedCharacterTragicLoveStory_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterTragicLoveStory_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name="character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    private String name;
    private String description;
}