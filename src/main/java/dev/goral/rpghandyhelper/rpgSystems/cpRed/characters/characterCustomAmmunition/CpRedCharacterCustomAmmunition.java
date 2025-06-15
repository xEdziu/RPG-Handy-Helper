package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CpRedCharacterCustomAmmunition {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomAmmunition_sequence",
            sequenceName = "cpRedCharacterCustomAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomAmmunition_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters character;

    @ManyToOne
    @JoinColumn(
            name = "customAmmunition_id",
            referencedColumnName = "id"
    )
    private CpRedCustomAmmunition customAmmunition;
    private CpRedCharacterItemStatus status;
    private Integer amount;
}
