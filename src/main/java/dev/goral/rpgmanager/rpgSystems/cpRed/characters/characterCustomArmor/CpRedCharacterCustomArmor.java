package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customArmors.CpRedCustomArmors;
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
public class CpRedCharacterCustomArmor {
    @Id
    @SequenceGenerator(
        name = "cpRedCharacterCustomArmor_sequence",
        sequenceName = "cpRedCharacterCustomArmor_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomArmor_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "armor_id",
            referencedColumnName = "id",
            nullable = false

    )
    private CpRedCustomArmors armorId;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
    private String description;
}