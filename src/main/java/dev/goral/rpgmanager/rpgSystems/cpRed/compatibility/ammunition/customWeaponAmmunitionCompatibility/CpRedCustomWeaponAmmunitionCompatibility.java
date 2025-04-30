package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.customWeaponAmmunitionCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
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

public class CpRedCustomWeaponAmmunitionCompatibility {
    @Id
    @SequenceGenerator(
            name = "cpRedCustomWeaponAmmunitionCompatibility_sequence",
            sequenceName = "cpRedCustomWeaponAmmunitionCompatibility_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomWeaponAmmunitionCompatibility_sequence"
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
            name = "ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedAmmunition AmmunitionId;

    public CpRedCustomWeaponAmmunitionCompatibility(CpRedCustomWeapons weapon, CpRedAmmunition ammunition) {
        this.customWeaponId = weapon;
        this.AmmunitionId = ammunition;
    }
}