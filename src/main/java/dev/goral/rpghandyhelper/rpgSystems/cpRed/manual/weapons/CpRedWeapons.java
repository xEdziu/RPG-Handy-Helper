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
    private Integer damage; //quantity of d6
    private Integer magazineCapacity;
    @ManyToOne
    @JoinColumn(
            name="standard_ammunition_id",
            referencedColumnName = "id"
    )
    private CpRedAmmunition standardAmmunitionId;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private Boolean isModifiable;
    private Short modSlots;
    @Column(length = 500)
    private String description;



}
