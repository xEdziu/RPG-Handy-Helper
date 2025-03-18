package dev.goral.rpgmanager.rpgSystems;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RpgSystemsDTO {
    private Long id;
    private String name;
    private String description;

    public RpgSystemsDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
