package dev.goral.rpgmanager.rpgSystems;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpgSystemsDTO {
    private Long id;
    private String name;
    private String description;

    public RpgSystemsDTO() {
    }

    public RpgSystemsDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RpgSystemsDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "rpgSystemsDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
