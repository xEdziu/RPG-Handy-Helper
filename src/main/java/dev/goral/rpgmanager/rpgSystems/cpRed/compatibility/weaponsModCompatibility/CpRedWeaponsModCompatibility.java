package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.weaponsModCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weaponMods.CpRedWeaponMods;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
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
public class CpRedWeaponsModCompatibility {
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
            name = "mod_id",
            referencedColumnName = "id"
    )
    private CpRedWeaponMods modId;
    @ManyToOne
    @JoinColumn(
            name = "weapon_id",
            referencedColumnName = "id"
    )
    private CpRedWeapons weaponId;
}