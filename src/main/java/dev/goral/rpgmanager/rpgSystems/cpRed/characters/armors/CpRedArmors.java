package dev.goral.rpgmanager.rpgSystems.cpRed.characters.armors;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.items.CpRedItemsAvailability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedArmors {
    @Id
    @SequenceGenerator(
        name = "cpRedArmors_sequence",
        sequenceName = "cpRedArmors_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cpRedArmors_sequence"
    )
    private Long id;
    @Enumerated(EnumType.STRING)
    private CpRedArmorsType type;
    private int ob;
    private int penalty;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}