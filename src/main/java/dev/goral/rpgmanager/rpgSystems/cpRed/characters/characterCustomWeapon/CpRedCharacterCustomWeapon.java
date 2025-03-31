package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeapon;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeaponQuality;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeapons.CpRedCustomWeapons;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CpRedCharacterCustomWeapon {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomWeaponIdGenerator",
            sequenceName = "cpRedCharacterCustomWeaponIdGenerator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomWeaponIdGenerator"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "weapon_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCustomWeapons weaponId;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterWeaponQuality quality;
    private int ammunition;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
    private String description;
}