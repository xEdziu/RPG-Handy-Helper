package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.items.CpRedItemsAvailability;
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
    private CpRedSkills requireSkillId;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponsType type;
    private int damage; //quantity of d6
    private int magazineCapacity;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponsAmmunition ammunition;
    private short la;
    private short hands;
    private boolean isHidden;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private String description;



}
