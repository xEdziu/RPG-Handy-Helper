package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition;

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
public class CpRedAmmunitionCompatibility {
    @Id
    @SequenceGenerator(
            name = "cpRedAmmunitionCompatibility_sequence",
            sequenceName = "cpRedAmmunitionCompatibility_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedAmmunitionCompatibility_sequence"
    )
    private Long id;

    private Long weaponId;
    private Long ammunitionId;

    private boolean isWeaponCustom;
    private boolean isAmmunitionCustom;
}
