package dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CpRedStats {
    @Id
    @SequenceGenerator(
            name = "cpRedStats_sequence",
            sequenceName = "cpRedStats_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedStats_sequence"
    )
    private Long id;
    private String name;
    private String tag;

    private boolean changeable;

    @Column(length = 500)
    private String description;

}