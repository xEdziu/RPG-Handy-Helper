package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors.CpRedArmorsType;
import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsAvailability;
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
public class CpRedCustomArmors {
    @Id
    @SequenceGenerator(
            name = "cpRedCustomArmors_sequence",
            sequenceName = "cpRedCustomArmors_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomArmors_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "game_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Game gameId;
    private String name;
    @Enumerated(EnumType.STRING)
    private CpRedArmorsType type;
    private int armorPoints;
    private int penalty;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}