package dev.goral.rpghandyhelper.rpgSystems;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpgSystems {
    @Id
    @SequenceGenerator(
            name = "rpgSystems_sequence",
            sequenceName = "rpgSystems_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rpgSystems_sequence"
    )
    private Long id;
    private String name;

    @Column(length = 1000)
    private String description;

    public RpgSystems(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
