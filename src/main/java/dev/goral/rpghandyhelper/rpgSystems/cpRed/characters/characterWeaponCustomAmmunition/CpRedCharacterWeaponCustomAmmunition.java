package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponCustomAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeapon;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
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
public class CpRedCharacterWeaponCustomAmmunition {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterWeaponCustomAmmunition_sequence",
            sequenceName = "cpRedCharacterWeaponCustomAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterWeaponCustomAmmunition_sequence"
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
            name = "custom_ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedCustomAmmunition customAmmunitionId;
}