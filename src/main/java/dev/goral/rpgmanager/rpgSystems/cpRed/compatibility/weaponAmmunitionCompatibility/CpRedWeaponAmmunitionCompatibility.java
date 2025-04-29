package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.weaponAmmunitionCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CpRedWeaponAmmunitionCompatibility {
    @Id
    @SequenceGenerator(
            name = "cpRedWeaponAmmunitionCompatibility_sequence",
            sequenceName = "cpRedWeaponAmmunitionCompatibility_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
           strategy = GenerationType.SEQUENCE,
              generator = "cpRedWeaponAmmunitionCompatibility_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedAmmunition ammunition;
    @ManyToOne
    @JoinColumn(
            name = "weapon_id",
            referencedColumnName = "id"
    )
    private CpRedWeapons weaponId;
}
