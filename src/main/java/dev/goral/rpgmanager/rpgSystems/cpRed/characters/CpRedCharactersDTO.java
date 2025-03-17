package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import lombok.Getter;

@Getter
public class CpRedCharactersDTO {
    private final Long id;
    private final String game;
    private final String user;
    private final String name;
    private final String nickname;
    private final String type;
    private final Integer expAll;
    private final Integer expAvailable;
    private final Integer cash;

    public CpRedCharactersDTO(Long id,
                              String game,
                              String user,
                              String name,
                              String nickname,
                              String type,
                              Integer expAll,
                              Integer expAvailable,
                              Integer cash) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.name = name;
        this.nickname = nickname;
        this.type = type;
        this.expAll = expAll;
        this.expAvailable = expAvailable;
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "CpRedCharactersDTO{" +
                "id=" + id +
                ", game='" + game + '\'' +
                ", user='" + user + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type='" + type + '\'' +
                ", expAll=" + expAll +
                ", expAvailable=" + expAvailable +
                ", cash=" + cash +
                '}';
    }
}
