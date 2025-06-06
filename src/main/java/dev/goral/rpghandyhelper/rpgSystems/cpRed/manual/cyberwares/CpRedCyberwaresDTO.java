package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCyberwaresDTO {
    private String name;
    private String mountPlace;
    private String requirements;
    private String humanityLoss;
    private int size;
    private String installationPlace;
    private int price;
    private String availability;
    private String description;
}