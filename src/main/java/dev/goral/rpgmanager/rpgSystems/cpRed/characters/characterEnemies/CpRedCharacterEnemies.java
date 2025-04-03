package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterEnemies;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
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
public class CpRedCharacterEnemies {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterEnemies_sequence",
            sequenceName = "cpRedCharacterEnemies_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterEnemies_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name="character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    private String name;
    private String whoIs;
    private String couseOfConflict;
    private String whatHas;
    private String intends;
    private String description;
}