package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customArmors;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.armors.CpRedArmorsType;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.items.CpRedItemsAvailability;
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
            name = "customArmors_sequence",
            sequenceName = "customArmors_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customArmors_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "game_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Game gameId;
    @Enumerated(EnumType.STRING)
    private CpRedArmorsType type;
    private int ob;
    private int penalty;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}