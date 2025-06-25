package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

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
public class CpRedCharacterWeaponMod {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterWeaponMod_sequence",
            sequenceName = "cpRedCharacterWeaponMod_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterWeaponMod_sequence"
    )
    private Long id;

    private Long characterId;

    private Long characterWeaponId;
    private Boolean isCharacterWeaponCustom;

    private Long weaponModId;
    private Boolean isWeaponModCustom;

    private Integer sizeTaken;
}
