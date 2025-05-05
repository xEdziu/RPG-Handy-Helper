package dev.goral.rpgmanager.scheduler.service;

import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.scheduler.dto.request.CreateSchedulerRequest;
import dev.goral.rpgmanager.scheduler.dto.request.SetFinalDecisionRequest;
import dev.goral.rpgmanager.scheduler.dto.request.SubmitAvailabilityRequest;
import dev.goral.rpgmanager.scheduler.dto.response.PlayerAvailabilityResponse;
import dev.goral.rpgmanager.scheduler.dto.response.SchedulerResponse;
import dev.goral.rpgmanager.scheduler.dto.response.SuggestedSlotResponse;
import dev.goral.rpgmanager.scheduler.entity.*;
import dev.goral.rpgmanager.scheduler.enums.AvailabilityType;
import dev.goral.rpgmanager.scheduler.repository.SchedulerRepository;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new scheduler based on the provided request and user principal and validates the request using
     * {@link #validateRequest(CreateSchedulerRequest)}.
     *
     * @param request   An object containing the details of the scheduler to be created.
     * @param principal A principal object representing the currently authenticated user.
     * @return {@link SchedulerResponse} An object containing the details of the created scheduler.
     * @throws IllegalArgumentException If the user is not the creator of the game,
     * if the user is not a participant of the game, or if the request is invalid.
     */
    @Transactional
    public SchedulerResponse createScheduler(CreateSchedulerRequest request, @AuthenticationPrincipal Principal principal) {

        User creator = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika: " + principal.getName()));

        // Sprawdź, czy użytkownik jest twórcą gry
        if (!creator.getId().equals(request.getCreatorId())) {
            throw new IllegalArgumentException("Użytkownik nie jest twórcą gry");
        }

        // Sprawdź, czy użytkownik jest uczestnikiem gry
        if (!gameUsersRepository.existsByGameIdAndUserId(request.getGameId(), creator.getId())) {
            throw new IllegalArgumentException("Użytkownik nie jest uczestnikiem gry");
        }

        // Sprawdź, czy gra istnieje
        if (!gameRepository.existsById(request.getGameId())) {
            throw new IllegalArgumentException("Nie znaleziono gry o id: " + request.getGameId());
        }

        // Sprawdź, czy twórca harmonogramu to GameMaster
        if (!gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gry o id: " + request.getGameId()))
                .getGameMaster().getId().equals(creator.getId())) {
            throw new IllegalArgumentException("Tylko GameMaster może stworzyć harmonogram");
        }

        // Walidacja
        validateRequest(request);

        // Utwórz encję Scheduler
        Scheduler scheduler = new Scheduler();
        scheduler.setTitle(request.getTitle());
        scheduler.setDeadline(request.getDeadline());
        scheduler.setMinimumSessionDurationMinutes(request.getMinimumSessionDurationMinutes());

        // Pobierz Game i Creator
        scheduler.setGame(gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gry o id: " + request.getGameId())));

        scheduler.setCreator(userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono twórcy gry: " + request.getCreatorId())));

        // Zakresy dat
        scheduler.setDateRanges(
                request.getDateRanges().stream()
                        .map(dto -> new SchedulerDateRange(
                                null,
                                dto.getStartDate(),
                                dto.getEndDate(),
                                dto.getStartTime(),
                                dto.getEndTime(),
                                scheduler
                        ))
                        .collect(Collectors.toList())
        );

        // Uczestnicy
        scheduler.setParticipants(
                request.getParticipantIds().stream()
                        .map(pid -> {
                            SchedulerParticipant participant = new SchedulerParticipant();
                            participant.setScheduler(scheduler);
                            participant.setPlayer(userRepository.findById(pid)
                                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gracza: " + pid)));
                            participant.setNotifiedByEmail(true);
                            return participant;
                        })
                        .collect(Collectors.toList())
        );

        // Zapisz scheduler
        Scheduler saved = schedulerRepository.save(scheduler);

        // Mapowanie do DTO
        return SchedulerResponseMapper.mapToDto(saved);
    }

    /**
     * Validates the given request for creating a scheduler.
     * @param request The request to validate.
     * @throws IllegalArgumentException If the request is invalid.
     */
    private void validateRequest(CreateSchedulerRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Tytuł nie może być pusty");
        }

        if (request.getDeadline() == null || request.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Termin wypełnienia dostępności nie może być w przeszłości");
        }

        if (request.getMinimumSessionDurationMinutes() == null || request.getMinimumSessionDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Minimalny czas sesji musi być większy od 0");
        }

        if (request.getDateRanges() == null || request.getDateRanges().isEmpty()) {
            throw new IllegalArgumentException("Musi być podany przynajmniej jeden zakres dat");
        }

        for (CreateSchedulerRequest.DateRangeDto range : request.getDateRanges()) {
            if (range.getStartDate() == null || range.getEndDate() == null ||
                    range.getStartTime() == null || range.getEndTime() == null) {
                throw new IllegalArgumentException("Zakresy dat i godzin nie mogą być puste");
            }

            if (range.getStartDate().isAfter(range.getEndDate())) {
                throw new IllegalArgumentException("Data początkowa nie może być po dacie końcowej");
            }

            if (range.getStartDate().isEqual(range.getEndDate()) && range.getStartTime().isAfter(range.getEndTime())) {
                throw new IllegalArgumentException("Dla tej samej daty, czas początkowy nie może być po czasie końcowym");
            }
        }

        if (request.getParticipantIds() == null || request.getParticipantIds().isEmpty()) {
            throw new IllegalArgumentException("Musi być przynajmniej jeden uczestnik");
        }

        long uniqueCount = request.getParticipantIds().stream().distinct().count();
        if (uniqueCount != request.getParticipantIds().size()) {
            throw new IllegalArgumentException("Na liście uczestników znajdują się duplikaty");
        }

        if (!request.getParticipantIds().contains(request.getCreatorId())) {
            throw new IllegalArgumentException("Twórca harmonogramu powinien być również jego uczestnikiem");
        }

        for (Long participantId : request.getParticipantIds()) {
            if (!this.gameUsersRepository.existsByGameIdAndUserId(request.getGameId(), participantId)) {
                throw new IllegalArgumentException("Uczestnik o id " + participantId + " nie jest uczestnikiem gry");
            }
        }
    }

    /**
     * Downloads all schedulers for a given game.
     *
     * @param gameId The ID of the game for which to download schedulers.
     * @param principal The user who is requesting the schedulers.
     * @return A list of schedulers as {@link SchedulerResponse} objects.
     * @throws IllegalArgumentException If the user does not have access to the game or if the game is not found.
     */
    public List<SchedulerResponse> getSchedulersByGame(Long gameId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        if (!gameUsersRepository.existsByGameIdAndUserId(gameId, user.getId())) {
            throw new IllegalArgumentException("Nie masz dostępu do tego harmonogramu");
        }

        return schedulerRepository.findByGameId(gameId).stream()
                .map(SchedulerResponseMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets a scheduler by its ID.
     *
     * @param schedulerId The ID of the scheduler to retrieve.
     * @param principal The user who is requesting the scheduler.
     * @return The scheduler as a {@link SchedulerResponse}.
     * @throws IllegalArgumentException If the user does not have access to the scheduler or if the scheduler is not found.
     */
    public SchedulerResponse getSchedulerById(Long schedulerId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!gameUsersRepository.existsByGameIdAndUserId(scheduler.getGame().getId(), user.getId())) {
            throw new IllegalArgumentException("Nie masz dostępu do tego harmonogramu");
        }

        return SchedulerResponseMapper.mapToDto(scheduler);
    }

    /**
     * Deletes a scheduler by its ID.
     *
     * @param schedulerId The ID of the scheduler to delete.
     * @param principal The user who is submitting the final decision.
     * @throws IllegalArgumentException If the user is not the creator of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public void deleteScheduler(Long schedulerId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!scheduler.getCreator().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Tylko twórca harmonogramu może go usunąć");
        }

        schedulerRepository.delete(scheduler);
    }

    /**
     * Sets the final decision for a given scheduler.
     *
     * @param request   The request containing the final decision details.
     * @param principal The user who is submitting the final decision.
     * @return The updated scheduler as a {@link SchedulerResponse}.
     * @throws IllegalArgumentException If the user is not the creator of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public SchedulerResponse setFinalDecision(SetFinalDecisionRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono harmonogramu o id: " + request.getSchedulerId()));

        if (!scheduler.getCreator().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Tylko twórca harmonogramu może ustawić ostateczny termin");
        }

        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        if (start == null || end == null) {
            throw new IllegalArgumentException("Daty nie mogą być puste");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Data rozpoczęcia nie może być po dacie zakończenia");
        }

        long minutes = Duration.between(start, end).toMinutes();
        if (minutes < scheduler.getMinimumSessionDurationMinutes()) {
            throw new IllegalArgumentException("Wybrany slot jest zbyt krótki");
        }

        // WALIDACJA: Czy mieści się w którymkolwiek slocie z dostępności uczestników?
        boolean isCovered = scheduler.getParticipants().stream()
                .flatMap(p -> p.getAvailabilitySlots().stream())
                .filter(s -> s.getAvailabilityType() != AvailabilityType.NO)
                .anyMatch(s -> !s.getStartDateTime().isAfter(start) &&
                        !s.getEndDateTime().isBefore(end));

        if (!isCovered) {
            throw new IllegalArgumentException("Żaden uczestnik nie jest dostępny w tym przedziale");
        }

        FinalDecision decision = new FinalDecision();
        decision.setStart(start);
        decision.setEnd(end);

        scheduler.setFinalDecision(decision);
        Scheduler saved = schedulerRepository.save(scheduler);
        return SchedulerResponseMapper.mapToDto(saved);
    }


    /**
     * Sets the availability for a given scheduler.
     *
     * @param request   The request containing the availability slots.
     * @param principal The user who is submitting the availability.
     * @throws IllegalArgumentException If the user is not a participant of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public void submitAvailability(SubmitAvailabilityRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono harmonogramu o id: " + request.getSchedulerId()));

        // Sprawdź, czy użytkownik jest uczestnikiem harmonogramu
        SchedulerParticipant participant = scheduler.getParticipants().stream()
                .filter(p -> p.getPlayer().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie jest uczestnikiem tego harmonogramu"));

        // Wyczyść poprzednie dostępności
        participant.getAvailabilitySlots().clear();

        // Dodaj nowe
        List<AvailabilitySlot> slots = request.getSlots().stream()
                .map(dto -> {
                    AvailabilitySlot slot = new AvailabilitySlot();
                    slot.setParticipant(participant);
                    slot.setStartDateTime(dto.getStartDateTime());
                    slot.setEndDateTime(dto.getEndDateTime());
                    slot.setAvailabilityType(dto.getAvailabilityType());
                    return slot;
                }).toList();

        participant.getAvailabilitySlots().addAll(slots);
    }

    /**
     * Gets the availability of a player for a given scheduler.
     *
     * @param schedulerId The ID of the scheduler.
     * @param principal   The user who is requesting the availability.
     * @return {@link PlayerAvailabilityResponse} The availability response containing the player's availability slots.
     * @throws IllegalArgumentException If the user is not a participant of the scheduler or if the scheduler is not found.
     */
    @Transactional(readOnly = true)
    public PlayerAvailabilityResponse getPlayerAvailability(Long schedulerId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika " + principal.getName()));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono harmonogramu o id: " + schedulerId));

        SchedulerParticipant participant = scheduler.getParticipants().stream()
                .filter(p -> p.getPlayer().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie jest uczestnikiem tego harmonogramu"));

        List<PlayerAvailabilityResponse.AvailabilitySlotDto> slots = participant.getAvailabilitySlots().stream()
                .map(s -> new PlayerAvailabilityResponse.AvailabilitySlotDto(
                        s.getStartDateTime(),
                        s.getEndDateTime(),
                        s.getAvailabilityType()
                )).toList();

        return new PlayerAvailabilityResponse(user.getId(), slots);
    }

    @Transactional(readOnly = true)
    public SuggestedSlotResponse suggestTimeSlots(Long schedulerId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!gameUsersRepository.existsByGameIdAndUserId(scheduler.getGame().getId(), user.getId())) {
            throw new IllegalArgumentException("Nie masz dostępu do tego harmonogramu");
        }

        int minDuration = scheduler.getMinimumSessionDurationMinutes();

        // Krok 1: Zgrupuj sloty per uczestnik i scal sąsiadujące
        Map<Long, List<AvailabilitySlot>> mergedByUser = scheduler.getParticipants().stream()
                .collect(Collectors.toMap(
                        p -> p.getPlayer().getId(),
                        p -> mergeUserSlots(p.getAvailabilitySlots())
                ));

        // Krok 2: Stwórz oś czasu z punktami start/stop z wagami
        List<TimePoint> timeline = new ArrayList<>();
        for (var entry : mergedByUser.entrySet()) {
            for (AvailabilitySlot slot : entry.getValue()) {
                double weight = switch (slot.getAvailabilityType()) {
                    case AvailabilityType.YES -> 1.0;
                    case AvailabilityType.MAYBE -> 0.5;
                    default -> 0.0;
                };
                if (weight > 0) {
                    timeline.add(new TimePoint(slot.getStartDateTime(), weight));
                    timeline.add(new TimePoint(slot.getEndDateTime(), -weight));
                }
            }
        }

        timeline.sort(Comparator.comparing(tp -> tp.time));

        // Krok 3: Przejdź po punktach i zbuduj przedziały
        List<SuggestedSlotResponse.TimeSlotDto> suggested = new ArrayList<>();
        double currentWeight = 0.0;
        LocalDateTime windowStart = null;

        for (TimePoint point : timeline) {
            currentWeight += point.delta;

            boolean isWindowStart = (windowStart == null && currentWeight >= 1.5); // prog można regulować
            boolean isWindowEnd = (windowStart != null && currentWeight < 1.5);

            if (isWindowStart) {
                windowStart = point.time;
            }

            if (isWindowEnd) {
                long minutes = Duration.between(windowStart, point.time).toMinutes();
                if (minutes >= minDuration) {
                    suggested.add(new SuggestedSlotResponse.TimeSlotDto(
                            windowStart,
                            point.time,
                            (int) Math.round(currentWeight)  // można też zwracać dokładnie
                    ));
                }
                windowStart = null;
            }
        }

        suggested.sort(Comparator.comparingInt(SuggestedSlotResponse.TimeSlotDto::getNumberOfAvailableParticipants).reversed());

        return new SuggestedSlotResponse(suggested);
    }

    private List<AvailabilitySlot> mergeUserSlots(List<AvailabilitySlot> slots) {
        List<AvailabilitySlot> sorted = slots.stream()
                .filter(s -> s.getAvailabilityType() != AvailabilityType.NO)
                .sorted(Comparator.comparing(AvailabilitySlot::getStartDateTime))
                .toList();

        List<AvailabilitySlot> result = new ArrayList<>();
        for (AvailabilitySlot slot : sorted) {
            if (result.isEmpty()) {
                result.add(slot);
                continue;
            }
            AvailabilitySlot last = result.getLast();
            long gap = Duration.between(last.getEndDateTime(), slot.getStartDateTime()).toMinutes();
            if (gap <= 10 && slot.getAvailabilityType() == last.getAvailabilityType()) {
                last.setEndDateTime(slot.getEndDateTime());
            } else {
                result.add(slot);
            }
        }
        return result;
    }


    private static class TimePoint {
        LocalDateTime time;
        double delta;

        public TimePoint(LocalDateTime time, double delta) {
            this.time = time;
            this.delta = delta;
        }
    }



}
