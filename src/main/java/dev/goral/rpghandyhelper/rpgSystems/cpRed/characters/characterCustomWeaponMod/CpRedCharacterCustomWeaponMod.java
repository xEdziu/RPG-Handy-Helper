package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeaponMod;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeapon;
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
public class CpRedCharacterCustomWeaponMod {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomWeaponMod_sequence",
            sequenceName = "cpRedCharacterCustomWeaponMod_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomWeaponMod_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "character_custom_weapon_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacterCustomWeapon characterCustomWeaponId;
    @ManyToOne
    @JoinColumn(
            name = "weapon_mod_id",
            referencedColumnName = "id"
    )
    private CpRedWeaponMods weaponModId;
}
