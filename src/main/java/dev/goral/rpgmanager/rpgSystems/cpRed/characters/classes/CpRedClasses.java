package dev.goral.rpgmanager.rpgSystems.cpRed.characters.classes;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CpRedClasses {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterClassesSequence",
            sequenceName = "cpRedCharacterClassesSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterClassesSequence"
    )
    private Long id;
    private String name;
    private String description;

    public CpRedClasses(Long id) {this.id=id;}
    public CpRedClasses(String name,
                        String description) {
        this.name=name;
        this.description=description;
    }

}
