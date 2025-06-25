package dev.goral.rpghandyhelper.scheduler.dto.common;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NextSession {
    String sessionName;
    String gameName;
    LocalTime startTime;
}
