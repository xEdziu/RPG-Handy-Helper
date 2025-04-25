package dev.goral.rpgmanager.rpgSystems.cpRed.characters.classes;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedClasses {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterClasses_sequence",
            sequenceName = "cpRedCharacterClasses_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterClasses_sequence"
    )
    private Long id;
    private String name;
    private String description;
}