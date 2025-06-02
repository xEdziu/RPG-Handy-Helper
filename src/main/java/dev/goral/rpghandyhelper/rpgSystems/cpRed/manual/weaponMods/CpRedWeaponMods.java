package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weaponMods;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
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
    private int price=-1;
    private int size=-1;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}