package dev.goral.rpgmanager.notes;

import lombok.Data;

@Data
public class GameNoteDto {
    private Long gameId;
    private Long userId;
    private String content;
}
