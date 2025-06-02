package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
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

    @Column(length = 500)
    private String description;

    private int pricePerBullet = -1;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
}