package dev.goral.rpghandyhelper.scheduler.service;

import dev.goral.rpghandyhelper.scheduler.dto.response.SchedulerResponse;
import dev.goral.rpghandyhelper.scheduler.entity.Scheduler;

import java.util.stream.Collectors;

public class SchedulerResponseMapper {

    public static SchedulerResponse mapToDto(Scheduler s) {
        return SchedulerResponse.builder()
                .id(s.getId())
                .title(s.getTitle())
                .deadline(s.getDeadline())
                .minimumSessionDurationMinutes(s.getMinimumSessionDurationMinutes())
                .gameId(s.getGame().getId())
                .creatorId(s.getCreator().getId())

                .dateRanges(
                        s.getDateRanges().stream()
                                .map(d -> new SchedulerResponse.DateRangeDto(
                                        d.getStartDate(),
                                        d.getEndDate()
                                )).collect(Collectors.toList())
                )

                .timeRanges(
                        s.getTimeRanges().stream()
                                .map(t -> new SchedulerResponse.TimeRangeDto(
                                        t.getStartTime(),
                                        t.getEndTime()
                                )).collect(Collectors.toList())
                )

                .participants(
                        s.getParticipants().stream()
                                .map(p -> new SchedulerResponse.ParticipantDto(
                                        p.getId(),
                                        p.getPlayer().getId(),
                                        p.isNotifiedByEmail()
                                )).collect(Collectors.toList())
                )

                .status(s.getStatus())

                .missingAvailabilitiesCount(
                        (int) s.getParticipants().stream()
                                .filter(p -> p.getAvailabilitySlots() == null || p.getAvailabilitySlots().isEmpty())
                                .count()
                )

                .finalDecision(
                        s.getFinalDecision() == null ? null :
                                new SchedulerResponse.FinalDecisionDto(
                                        s.getFinalDecision().getStart(),
                                        s.getFinalDecision().getEnd()
                                )
                )

                .googleCalendarLink(s.getGoogleCalendarLink())
                .emailsSent(s.isEmailsSent())

                .build();
    }
}