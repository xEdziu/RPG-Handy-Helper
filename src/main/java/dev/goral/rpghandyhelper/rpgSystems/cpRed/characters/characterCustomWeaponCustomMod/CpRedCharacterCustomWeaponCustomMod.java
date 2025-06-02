package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeaponCustomMod;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeapon;
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
public class CpRedCharacterCustomWeaponCustomMod {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomWeaponCustomMod_sequence",
            sequenceName = "cpRedCharacterCustomWeaponCustomMod_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomWeaponCustomMod_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "cpRedCharacterCustomWeapon_id",
            referencedColumnName = "id"
    )
    private CpRedCharacterCustomWeapon cpRedCharacterCustomWeaponId;
    @ManyToOne
    @JoinColumn(
            name = "cpRedCustomWeaponModId_id",
            referencedColumnName = "id"
    )
    private CpRedCustomWeaponMods customWeaponModId;
}