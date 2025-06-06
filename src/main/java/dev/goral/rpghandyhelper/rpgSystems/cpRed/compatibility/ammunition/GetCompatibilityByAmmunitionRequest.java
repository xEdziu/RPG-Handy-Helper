package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCompatibilityByAmmunitionRequest {
    private Long ammunitionId;
    private boolean isAmmunitionCustom;
}
