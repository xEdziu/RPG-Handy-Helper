package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

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

    private Long characterId;
    private Long characterWeaponId;
    private Boolean isCharacterWeaponCustom;
    private Long ammunitionId;
    private Boolean isAmmunitionCustom;
    private Integer amount;
}
