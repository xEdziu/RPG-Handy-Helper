package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterEquipment;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.item.CpRedItemAvailability;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.Equipment.CpRedEquipment;
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
public class CpRedCharacterEquipment {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterEquipmentSequence",
            sequenceName = "cp_red_character_equipment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterEquipmentSequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "item_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedEquipment itemId;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters characterId;
    @Enumerated(EnumType.STRING)
    private CpRedItemAvailability availability;
    private String description;
}