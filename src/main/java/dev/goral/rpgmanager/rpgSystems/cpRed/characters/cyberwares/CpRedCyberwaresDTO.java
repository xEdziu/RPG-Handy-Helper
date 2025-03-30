package dev.goral.rpgmanager.rpgSystems.cpRed.characters.cyberwares;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCyberwaresDTO {
    private long id;
    private String name;
    private String mountPlace;
    private String requirements;
    private String uc;
    private String installationPlace;
    private int price;
    private String availability;
    private String description;
}