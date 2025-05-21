package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeaponCustomAmmunition;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeapon;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
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
public class CpRedCharacterCustomWeaponCustomAmmunition {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomWeaponCustomAmmunition_sequence",
            sequenceName = "cpRedCharacterCustomWeaponCustomAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomWeaponCustomAmmunition_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "character_custom_weapon_id",
            referencedColumnName = "id"
    )
    private CpRedCharacterCustomWeapon characterCustomWeapon;
    @ManyToOne
    @JoinColumn(
            name = "custom_ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedCustomAmmunition customAmmunitionId;
}