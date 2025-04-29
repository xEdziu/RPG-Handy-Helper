package dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition;

import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsAvailability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CpRedAmmunition {
    @Id
    @SequenceGenerator(
            name = "cpRedAmmunition_sequence",
            sequenceName = "cpRedAmmunition_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedAmmunition_sequence"
    )
    private Long id;
    private String name;
    private String description;
    private int pricePerBullet;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
}