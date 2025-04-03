package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomEquipment;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.customEquipments.CpRedCustomEquipments;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.items.CpRedItemsAvailability;
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
public class CpRedCharacterCustomEquipment {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomEquipment_sequence",
            sequenceName = "cpRedCharacterCustomEquipment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomEquipment_sequence"
    )
    private long id;
    @ManyToOne
    @JoinColumn(
            name = "equipment_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCustomEquipments equipmentId;
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