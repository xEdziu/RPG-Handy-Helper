package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.armors;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
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
    private String name;
    private int armorPoints=-1;
    private int penalty=-1;
    private int price=-1;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;
}