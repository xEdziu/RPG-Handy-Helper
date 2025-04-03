package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterClasses;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.classes.CpRedClasses;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CpRedCharacterClasses {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterClasses_sequence",
            sequenceName = "cpRedCharacterClasses_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterClasses_sequence"
    )

    private Long id;
    private short classLevel;

    @ManyToOne
    @JoinColumn(name = "character_id",
            referencedColumnName = "id")
    private CpRedCharacters characterId;

    @ManyToOne
    @JoinColumn(name = "class_id",
            referencedColumnName = "id")
    private CpRedClasses classId;

    public CpRedCharacterClasses(Long id,
                                 short classLevel,
                                 CpRedCharacters characterId,
                                 CpRedClasses classId) {
        this.id = id;
        this.classLevel = classLevel;
        this.characterId = characterId;
        this.classId = classId;
    }
}