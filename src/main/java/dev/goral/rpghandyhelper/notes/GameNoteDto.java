package dev.goral.rpghandyhelper.notes;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GameNoteDto {
    private Long gameId;
    private Long userId;
    private String title;
    private String content;
}
