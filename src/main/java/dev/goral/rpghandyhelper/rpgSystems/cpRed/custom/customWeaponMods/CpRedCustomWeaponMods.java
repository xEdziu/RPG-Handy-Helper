package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeaponMods;

import dev.goral.rpghandyhelper.game.Game;
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
public class CpRedCustomWeaponMods {
    @Id
    @SequenceGenerator(
            name = "cpRedCustomWeaponMods_sequence",
            sequenceName = "cpRedCustomWeaponMods_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomWeaponMods_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "game_id",
            referencedColumnName = "id"
    )
    private Game gameId;
    private String name;
    private int price;
    private int size;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}