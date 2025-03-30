package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

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
public class CpRedCyberwares {
    @Id
    @SequenceGenerator(
        name = "cpRedCyberware_sequence",
        sequenceName = "cpRedCyberware_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCyberware_sequence"
    )
    private Long id;
    private String name;

}
