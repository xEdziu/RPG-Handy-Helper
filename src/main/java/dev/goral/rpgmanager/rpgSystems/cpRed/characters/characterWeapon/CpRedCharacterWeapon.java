package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeapon;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapon.CpRedWeapon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCharacterWeapon {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterWeapon_sequence",
            sequenceName = "cpRedCharacterWeapon_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterWeapon_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "weapon_id",
            referencedColumnName = "id"
    )
    private CpRedWeapon weaponId;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters characterId;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterWeaponQuality quality;
    private int ammunition;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterWeaponStatus status;
    private String description;
}
