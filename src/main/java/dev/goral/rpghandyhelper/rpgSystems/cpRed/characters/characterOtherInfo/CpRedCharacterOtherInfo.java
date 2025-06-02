package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo;

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
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    private String notes;
    private String addictions;
    private String reputation;
    private String style;
    private String classLifePath;
    private String accommodation;
    private int rental;
    private String livingStandard;
}