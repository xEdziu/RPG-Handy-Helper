package dev.goral.rpgmanager.rpgSystems.cpRed.characters.Equipment;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.item.CpRedItemAvailability;
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
public class CpRedEquipment {
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
    private CpRedItemAvailability availability;
    private String description;
}