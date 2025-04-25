package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterFriends;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCharacterFriends {
    @Id
    @SequenceGenerator(
            name="cpRedCharacterFriends_sequence",
            sequenceName = "cpRedCharacterFriends_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterFriends_sequence"
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
