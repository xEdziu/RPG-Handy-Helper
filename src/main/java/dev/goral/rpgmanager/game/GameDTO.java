package dev.goral.rpgmanager.game;

import lombok.Getter;

@Getter
public class GameDTO {

    private final Long id;
    private final String name;
    private final String description;
    private final String gameMaster;

    public GameDTO(Long id,
                   String name,
                   String description,
                   String gameMaster) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameMaster = gameMaster;
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gameMaster='" + gameMaster + '\'' +
                '}';
    }
}
