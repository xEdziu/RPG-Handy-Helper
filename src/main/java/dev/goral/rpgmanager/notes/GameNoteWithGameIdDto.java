package dev.goral.rpgmanager.notes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@ToString
public class GameNoteWithGameIdDto {
    private Long id;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String gameName;
    private Long gameId;
    private String rpgSystemName;
}
