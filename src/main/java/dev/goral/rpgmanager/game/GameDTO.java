package dev.goral.rpgmanager.game;

import lombok.Getter;

@Getter
public class GameDTO {

    private final Long id;
    private final String name;
    private final String description;
    private final String gameMaster;
    private final String rpgSystem;

    public GameDTO(Long id,
                   String name,
                   String description,
                   String gameMaster,
                   String rpgSystem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameMaster = gameMaster;
        this.rpgSystem = rpgSystem;
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gameMaster='" + gameMaster + '\'' +
                ", rpgSystem='" + rpgSystem + '\'' +
                '}';
    }
}
