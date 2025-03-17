package dev.goral.rpgmanager.cp_red_character_classes;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.goral.rpgmanager.cp_red_classes.CpRedClasses;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.type.descriptor.jdbc.SmallIntJdbcType;


@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CpRedCharacterClasses {
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
    private short classLevel;
    @ManyToOne
    @JoinColumn(name = "character_id", referencedColumnName = "id")
    private CpRedClasses characterId;
    private int classId; //relationship

    public CpRedCharacterClasses(Long id) {this.id=id;}
    public CpRedCharacterClasses(short classLevel,
                                 CpRedClasses characterId,
                                 int classId) {
        this.classLevel=classLevel;
        this.characterId=characterId;
        this.classId=classId;
    }







}
