package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weaponMods;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.items.CpRedItemsAvailability;
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
public class CpRedWeaponMods {
    @Id
    @SequenceGenerator(
            name = "cpRedWeaponMods_sequence",
            sequenceName = "cpRedWeaponMods_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedWeaponMods_sequence"
    )
    private Long id;
    private String name;
    private int price;
    private int size;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}