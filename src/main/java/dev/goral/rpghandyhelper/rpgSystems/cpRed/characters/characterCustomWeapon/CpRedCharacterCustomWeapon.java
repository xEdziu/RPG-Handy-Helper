package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsQuality;
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
    private CpRedCustomWeapons baseCustomWeapon;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters character;
    private Integer dmg;
    private Integer magazineCapacity;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
    private String description;
}