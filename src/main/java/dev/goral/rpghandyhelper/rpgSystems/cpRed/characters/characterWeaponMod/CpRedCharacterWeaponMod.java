package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeapon;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weaponMods.CpRedWeaponMods;
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
    @ManyToOne
    @JoinColumn(
            name = "character_weapon_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacterWeapon characterWeaponId;
    @ManyToOne
    @JoinColumn(
            name = "weapon_mod_id",
            referencedColumnName = "id"
    )
    private CpRedWeaponMods weaponModId;
}
