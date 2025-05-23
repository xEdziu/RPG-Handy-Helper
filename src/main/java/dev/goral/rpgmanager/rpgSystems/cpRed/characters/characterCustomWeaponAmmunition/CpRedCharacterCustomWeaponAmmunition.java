package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeaponAmmunition;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeapon;
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
public class CpRedCharacterCustomWeaponAmmunition {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomWeaponAmmunition_sequence",
            sequenceName = "cpRedCharacterCustomWeaponAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomWeaponAmmunition_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "character_custom_weapon_id",
            referencedColumnName = "id"
    )
    private CpRedCharacterCustomWeapon characterCustomWeaponId;
    @ManyToOne
    @JoinColumn(
            name = "ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedAmmunition ammunitionId;
}