package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customEquipments;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
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
public class CpRedCustomEquipments {
    @Id
    @SequenceGenerator(
            name = "cpRedCustomEquipments_sequence",
            sequenceName = "cpRedCustomEquipments_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomEquipments_sequence"
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
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}