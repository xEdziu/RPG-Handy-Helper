package dev.goral.rpghandyhelper.scheduler.dto.common;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NextSession {
    String name;
    LocalTime startTime;
}
