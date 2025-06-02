package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod;

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
public class CpRedModCompatibility {
    @Id
    @SequenceGenerator(
            name = "cpRedModCompatibility_sequence",
            sequenceName = "cpRedModCompatibility_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedModCompatibility_sequence"
    )
    private Long id;

    private Long weaponId;
    private Long modId;

    private boolean isWeaponCustom;
    private boolean isModCustom;
}
