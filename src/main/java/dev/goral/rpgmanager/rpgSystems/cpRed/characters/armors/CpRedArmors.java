package dev.goral.rpgmanager.rpgSystems.cpRed.characters.armors;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedItemAvailability;
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
        name = "cpRedArmor_sequence",
        sequenceName = "cpRedArmor_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cpRedArmor_sequence"
    )
    private Long id;
    @Enumerated(EnumType.STRING)
    private CpRedArmorsType type;
    private int ob;
    private int penalty;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemAvailability availability;
    private String description;
}