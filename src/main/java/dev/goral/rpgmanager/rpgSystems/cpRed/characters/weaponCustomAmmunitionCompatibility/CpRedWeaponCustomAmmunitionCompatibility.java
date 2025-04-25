package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weaponCustomAmmunitionCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customAmmunition.CpRedCustomAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons.CpRedWeapons;
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
public class CpRedWeaponCustomAmmunitionCompatibility {
    @Id
    @SequenceGenerator(
            name = "cpRedWeaponCustomAmmunitionCompatibility_sequence",
            sequenceName = "cpRedWeaponCustomAmmunitionCompatibility_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedWeaponCustomAmmunitionCompatibility_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "weapon_id",
            referencedColumnName = "id"
    )
    private CpRedWeapons weaponId;
    @ManyToOne
    @JoinColumn(
            name = "custom_ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedCustomAmmunition customAmmunitionId;
}
