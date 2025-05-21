package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCyberwares;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCustomCyberwaresDTO {
    private Long gameId;
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