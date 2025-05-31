package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeaponAmmunition;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeapon;
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
public class CpRedCharacterWeaponAmmunition {
    @Id
    @SequenceGenerator(
            name = "characterWeaponAmmunition_sequence",
            sequenceName = "characterWeaponAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "characterWeaponAmmunition_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "character_weapon_id",
            referencedColumnName = "id"
    )
    private CpRedCharacterWeapon characterWeaponId;
    @ManyToOne
    @JoinColumn(
            name="ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedWeapons ammunitionId;
    private int amount;
}
