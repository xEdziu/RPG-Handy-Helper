package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCustomWeaponsDTO {
    private Long id;
    private Long gameId;
    private String name;
    private Long requiredSkillId;
    private String type;
    private Integer damage;
    private Integer magazineCapacity;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    private String quality;
    private Integer price;
    private String availability;
    private Boolean isModifiable;
    private Short modSlots;
    @Column(length = 500)
    private String description;
}