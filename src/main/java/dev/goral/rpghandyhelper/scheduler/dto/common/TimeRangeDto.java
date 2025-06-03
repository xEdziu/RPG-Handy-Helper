package dev.goral.rpghandyhelper.scheduler.dto.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Builder
@Data
public class TimeRangeDto {
    private LocalTime startTime;
    private LocalTime endTime;
}
