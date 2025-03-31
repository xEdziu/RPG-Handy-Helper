package dev.goral.rpgmanager.rpgSystems.cpRed.characters.Equipments;

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
public class CpRedEquipments {
    @Id
    @SequenceGenerator(
            name = "cpRedEquipmentSequence",
            sequenceName = "cpRedEquipmentSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy= GenerationType.SEQUENCE,
            generator = "cpRedEquipmentSequence"
    )
    private long id;
    private String name;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}