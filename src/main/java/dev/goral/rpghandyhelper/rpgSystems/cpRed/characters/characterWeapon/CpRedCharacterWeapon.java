package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsQuality;
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
            name = "base_weapon_id",
            referencedColumnName = "id"
    )
    private CpRedWeapons baseWeapon;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters character;
    private Integer dmg;
    private Integer magazineCapacity;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    private Short freeModSlots;
    private CpRedCharacterItemStatus status;
    @Column(length = 500)
    private String description;
}
