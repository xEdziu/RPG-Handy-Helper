package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.equipments.CpRedEquipments;
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
            name = "cpRedCharacterEquipment_sequence",
            sequenceName = "cpRedCharacterEquipment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterEquipment_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "item_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedEquipments item;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters character;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;

    @Column(length = 1000)
    private String description;
}