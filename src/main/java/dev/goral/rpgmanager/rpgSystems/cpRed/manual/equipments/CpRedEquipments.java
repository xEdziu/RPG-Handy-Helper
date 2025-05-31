package dev.goral.rpgmanager.rpgSystems.cpRed.manual.equipments;

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
public class CpRedEquipments {
    @Id
    @SequenceGenerator(
            name = "cpRedEquipments_sequence",
            sequenceName = "cpRedEquipments_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy= GenerationType.SEQUENCE,
            generator = "cpRedEquipments_sequence"
    )
    private Long id;
    private String name;
    private int price=-1;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}