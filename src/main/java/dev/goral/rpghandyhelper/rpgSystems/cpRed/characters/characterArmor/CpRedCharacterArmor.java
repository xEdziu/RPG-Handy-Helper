package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.armors.CpRedArmors;
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
public class CpRedCharacterArmor {
    @Id
    @SequenceGenerator(
        name = "cpRedCharacterArmor_sequence",
        sequenceName = "cpRedCharacterArmor_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator= "cpRedCharacterArmor_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
        name = "armor_id",
        referencedColumnName = "id"
    )
    private CpRedArmors baseArmor;
    @ManyToOne
    @JoinColumn(
        name = "character_id",
        referencedColumnName = "id"
    )
    private CpRedCharacters character;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterArmorPlace place;
    private Integer currentArmorPoints;
    @Column(length = 500)
    private String description;
}
