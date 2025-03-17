package dev.goral.rpgmanager.game;

import lombok.Getter;

@Getter
public class GameDTO {

    private final Long id;
    private final String name;
    private final String description;
    private final Long gameMasterId;
    private final Long rpgSystemId;

    public GameDTO(Long id,
                   String name,
                   String description,
                   Long gameMasterId,
                   Long rpgSystemId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameMasterId = gameMasterId;
        this.rpgSystemId = rpgSystemId;
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gameMaster='" + gameMasterId + '\'' +
                ", rpgSystem='" + rpgSystemId + '\'' +
                '}';
    }
}
