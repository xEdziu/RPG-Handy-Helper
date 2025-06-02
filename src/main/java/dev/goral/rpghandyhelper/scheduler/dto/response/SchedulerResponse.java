package dev.goral.rpghandyhelper.scheduler.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import dev.goral.rpghandyhelper.scheduler.enums.SchedulerStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchedulerResponse {
    private Long id;
    private String title;
    private LocalDateTime deadline;
    private Integer minimumSessionDurationMinutes;
    private Long gameId;
    private Long creatorId;
    private List<DateRangeDto> dateRanges;
    private List<TimeRangeDto> timeRanges;
    private List<ParticipantDto> participants;
    private FinalDecisionDto finalDecision;
    private SchedulerStatus status;
    private int missingAvailabilitiesCount;
    private String googleCalendarLink;
    private boolean emailsSent;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateRangeDto {
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRangeDto {
        private LocalTime startTime;
        private LocalTime endTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantDto {
        private Long id;
        private Long playerId;
        private boolean notifiedByEmail;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinalDecisionDto {
        private LocalDateTime start;
        private LocalDateTime end;
    }
}