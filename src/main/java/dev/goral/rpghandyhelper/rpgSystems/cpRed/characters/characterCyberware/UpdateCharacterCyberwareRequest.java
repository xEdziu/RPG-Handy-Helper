package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCharacterCyberwareRequest {
    @Column(length = 1000)
    private String description;
}
