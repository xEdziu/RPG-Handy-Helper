package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.Mod.weaponsCustomModCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods.CpRedCustomWeaponMods;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weaponMods.CpRedWeaponMods;
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
public class CpRedWeaponsCustomModCompatibility {
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
            name = "weapon_mod_id",
            referencedColumnName = "id"
    )
    private CpRedWeaponMods weaponModId;
    @ManyToOne
    @JoinColumn(
            name = "custom_mod_id",
            referencedColumnName = "id"
    )
    private CpRedCustomWeaponMods customModId;
}