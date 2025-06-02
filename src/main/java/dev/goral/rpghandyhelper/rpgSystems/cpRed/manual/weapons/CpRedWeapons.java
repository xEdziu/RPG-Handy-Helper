package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsQuality;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkills;
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
    private String name;
    private int damage = -1; //quantity of d6
    private int magazineCapacity = -1;
    @ManyToOne
    @JoinColumn(
            name="standard_ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedAmmunition standardAmmunitionId;
    private short numberOfAttacks = -1;
    private short handType = -1;
    private boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    private int price = -1;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private boolean isModifiable;
    @Column(length = 500)
    private String description;



}
