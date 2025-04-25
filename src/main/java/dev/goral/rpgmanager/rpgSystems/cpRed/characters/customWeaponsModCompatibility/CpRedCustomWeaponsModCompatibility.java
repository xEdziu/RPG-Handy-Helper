package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeaponsModCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeaponMods.CpRedCustomWeaponMods;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeapons.CpRedCustomWeapons;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.weaponMods.CpRedWeaponMods;
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
public class CpRedCustomWeaponsModCompatibility {
    @Id
    @SequenceGenerator(
            name = "cpRedWeaponsModCompatibility_sequence",
            sequenceName = "cpRedWeaponsModCompatibility_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedWeaponsModCompatibility_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "custom_weapon_id",
            referencedColumnName = "id"
    )
    private CpRedCustomWeapons customWeaponId;
    @ManyToOne
    @JoinColumn(
            name = "mod_id",
            referencedColumnName = "id"
    )
    private CpRedWeaponMods modId;
}