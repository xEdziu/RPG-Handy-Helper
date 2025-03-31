package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterOtherInfo;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
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
public class CpRedCharacterOtherInfo {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterOtherInfo_sequence",
            sequenceName = "cpRedCharacterOtherInfo_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterOtherInfo_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    private String notes;
    private String criticalInjuries;
    private String addictions;
    private String reputation;
    private String style;
    private String classLifePath;
    private String accomodation;
    private int rental;
    private String livingStandard;
}