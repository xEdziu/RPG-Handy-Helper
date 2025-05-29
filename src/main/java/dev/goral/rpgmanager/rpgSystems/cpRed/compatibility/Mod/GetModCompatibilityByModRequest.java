package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.Mod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetModCompatibilityByModRequest {
    private Long modId;
    private boolean isModCustom;
}
