package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.customWeaponsCustomModCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods.CpRedCustomWeaponMods;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
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
public class CpRedCustomWeaponsCustomModCompatibility {
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
            name = "custom_mod_id",
            referencedColumnName = "id"
    )
    private CpRedCustomWeaponMods customModId;
}