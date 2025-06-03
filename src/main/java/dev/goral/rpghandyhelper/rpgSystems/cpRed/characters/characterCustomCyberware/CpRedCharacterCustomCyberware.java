package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares.CpRedCustomCyberwares;
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
public class CpRedCharacterCustomCyberware {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomCyberware_sequence",
            sequenceName = "cpRedCharacterCustomCyberware_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomCyberware_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "cyberware_id",
            referencedColumnName = "id"
    )
    private CpRedCustomCyberwares cyberwareId;

    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters characterId;

    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
    private String description;
}