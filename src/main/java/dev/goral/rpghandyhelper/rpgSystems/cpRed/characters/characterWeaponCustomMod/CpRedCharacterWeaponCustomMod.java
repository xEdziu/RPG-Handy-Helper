package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponCustomMod;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeapon;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeaponMods.CpRedCustomWeaponMods;
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
public class CpRedCharacterWeaponCustomMod {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterWeaponCustomMod_sequence",
            sequenceName = "cpRedCharacterWeaponCustomMod_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterWeaponCustomMod_sequence"
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
            name = "custom_weapon_mod_id",
            referencedColumnName = "id"
    )
    private CpRedCustomWeaponMods customWeaponModId;
}
