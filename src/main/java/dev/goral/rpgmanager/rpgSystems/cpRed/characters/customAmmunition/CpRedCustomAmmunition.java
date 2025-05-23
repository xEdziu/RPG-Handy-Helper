package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customAmmunition;

import dev.goral.rpgmanager.game.Game;
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
public class CpRedCustomAmmunition {
    @Id
    @SequenceGenerator(
            name = "cpRedCustomAmmunition_sequence",
            sequenceName = "cpRedCustomAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomAmmunition_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name="game_id",
            referencedColumnName = "id"
    )
    private Game gameId;
    private String name;
    private String description;
    private int pricePerBullet;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
}
