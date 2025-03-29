package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterArmor;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacterItemStatus;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.armor.CpRedArmor;
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
    private CpRedArmor armorId;
    @ManyToOne
    @JoinColumn(
        name = "character_id",
        referencedColumnName = "id"
    )
    private CpRedCharacters characterId;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
}
