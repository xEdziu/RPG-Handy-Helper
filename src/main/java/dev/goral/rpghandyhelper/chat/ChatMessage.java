package dev.goral.rpghandyhelper.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String from;
    private String content;
    private Boolean privateMessage;
}
