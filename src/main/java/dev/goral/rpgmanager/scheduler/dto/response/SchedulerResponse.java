package dev.goral.rpgmanager.scheduler.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.*;

/*
 * SchedulerResponse.java
 *
 * This class represents the response object for a scheduler.
 * It contains information about the scheduler, including its ID,
 * title, deadline, minimum session duration, game ID, creator ID,
 * date ranges, participants, final decision, and Google Calendar link.
 */
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
    private List<ParticipantDto> participants;
    private FinalDecisionDto finalDecision;
    private String googleCalendarLink;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateRangeDto {
        private LocalDate startDate;
        private LocalDate endDate;
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
