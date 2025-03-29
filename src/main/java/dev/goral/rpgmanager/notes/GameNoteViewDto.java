package dev.goral.rpgmanager.notes;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class GameNoteViewDto {
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
