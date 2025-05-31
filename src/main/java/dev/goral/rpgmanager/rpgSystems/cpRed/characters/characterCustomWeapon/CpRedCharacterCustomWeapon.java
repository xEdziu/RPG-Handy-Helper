package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomWeapon;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeaponHandType;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsQuality;
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
            name = "cpRedCharacterCustomWeapon_sequence",
            sequenceName = "cpRedCharacterCustomWeapon_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomWeapon_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "base_weapon_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCustomWeapons baseWeaponId;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    private int dmg;
    private int magazineCapacity;
    private short numberOfAttacks;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterWeaponHandType handType;
    private boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
    private String description;
}