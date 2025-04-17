package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeaponCustomAmmunitionCompatibility;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customAmmunition.CpRedCustomAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeapons.CpRedCustomWeapons;
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
public class CpRedCustomWeaponCustomAmmunitionCompatibility {
    @Id
    @SequenceGenerator(
            name = "cpRedCustomWeaponCustomAmmunitionCompatibility_sequence",
            sequenceName = "cpRedCustomWeaponCustomAmmunitionCompatibility_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomWeaponCustomAmmunitionCompatibility_sequence"
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
            name = "custom_ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedCustomAmmunition customAmmunitionId;
}