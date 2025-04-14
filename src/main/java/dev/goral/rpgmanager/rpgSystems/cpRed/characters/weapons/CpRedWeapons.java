package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.ammunition.CpRedAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.items.CpRedItemsAvailability;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.items.CpRedItemsQuality;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.skills.CpRedSkills;
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
public class CpRedWeapons {
    @Id
    @SequenceGenerator(
            name = "cpRedWeapons_sequence",
            sequenceName = "cpRedWeapons_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedWeapons_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name="skill_id",
            referencedColumnName = "id"
    )
    private CpRedSkills requiredSkillId;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponsType type;
    private int damage; //quantity of d6
    private int magazineCapacity;
    @ManyToOne
    @JoinColumn(
            name="standard_ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedAmmunition standardAmmunitionId;
    private short numberOfAttacks;
    private short handType;
    private boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private boolean isModifiable;
    private String description;



}
