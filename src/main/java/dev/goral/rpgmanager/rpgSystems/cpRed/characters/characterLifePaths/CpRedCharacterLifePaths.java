package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterLifePaths;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
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
public class CpRedCharacterLifePaths {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterLifePaths_sequence",
            sequenceName = "cpRedCharacterLifePaths_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterLifePaths_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters characterId;
    private String cultureOfOrigin;
    private String yourCharacter;
    private String clothingAndStyle;
    private String hair;
    private String mostValue;
    private String relationships;
    private String mostImportantPerson;
    private String mostImportantItem;
    private String familyBackground;
    private String familyEnvironment;
    private String familyCrisis;
    private String lifeGoals;
}
