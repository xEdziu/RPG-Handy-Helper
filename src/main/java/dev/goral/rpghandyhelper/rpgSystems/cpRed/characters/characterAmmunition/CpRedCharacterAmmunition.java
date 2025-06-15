package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
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
public class CpRedCharacterAmmunition {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterAmmunition_sequence",
            sequenceName = "cpRedCharacterAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterAmmunition_sequence"
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
            name = "ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedAmmunition ammunition;
    private CpRedCharacterItemStatus status;
    private Integer amount;
}
