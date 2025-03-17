package dev.goral.rpgmanager.cp_red_classes;


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
            name = "cp_red_character_classes_sequence",
            sequenceName = "cp_red_character_classes_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cp_red_character_classes_sequence"
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
