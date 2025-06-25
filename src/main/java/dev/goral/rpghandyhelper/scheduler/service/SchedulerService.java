package dev.goral.rpghandyhelper.scheduler.service;

import dev.goral.rpghandyhelper.email.EmailService;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.scheduler.dto.common.NextSession;
import dev.goral.rpghandyhelper.scheduler.dto.common.TimeRangeDto;
import dev.goral.rpghandyhelper.scheduler.dto.request.CreateSchedulerRequest;
import dev.goral.rpghandyhelper.scheduler.dto.request.EditSchedulerRequest;
import dev.goral.rpghandyhelper.scheduler.dto.request.SetFinalDecisionRequest;
import dev.goral.rpghandyhelper.scheduler.dto.request.SubmitAvailabilityRequest;
import dev.goral.rpghandyhelper.scheduler.dto.response.PlayerAvailabilityResponse;
import dev.goral.rpghandyhelper.scheduler.dto.response.SchedulerResponse;
import dev.goral.rpghandyhelper.scheduler.dto.response.SuggestedSlotResponse;
import dev.goral.rpghandyhelper.scheduler.entity.*;
import dev.goral.rpghandyhelper.scheduler.enums.AvailabilityType;
import dev.goral.rpghandyhelper.scheduler.enums.SchedulerStatus;
import dev.goral.rpghandyhelper.scheduler.repository.SchedulerRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    /**
     * Creates a new scheduler based on the provided request and user currentUser and validates the request using
     * {@link #validateRequest(CreateSchedulerRequest)}.
     *
     * @param request   An object containing the details of the scheduler to be created.
     * @param currentUser A currentUser object representing the currently authenticated user.
     * @return {@link SchedulerResponse} An object containing the details of the created scheduler.
     * @throws IllegalStateException If the user is not the creator of the game,
     * if the user is not a participant of the game, or if the request is invalid.
     */
    @Transactional
    public SchedulerResponse createScheduler(CreateSchedulerRequest request, @AuthenticationPrincipal User currentUser) {

        User creator = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika: " + currentUser.getUsername()));

        // Sprawdź, czy użytkownik jest twórcą gry
        if (!creator.getId().equals(request.getCreatorId())) {
            throw new IllegalStateException("Użytkownik nie jest twórcą gry");
        }

        // Sprawdź, czy użytkownik jest uczestnikiem gry
        if (!gameUsersRepository.existsByGameIdAndUserId(request.getGameId(), creator.getId())) {
            throw new IllegalStateException("Użytkownik nie jest uczestnikiem gry");
        }

        // Sprawdź, czy gra istnieje
        if (!gameRepository.existsById(request.getGameId())) {
            throw new IllegalStateException("Nie znaleziono gry o id: " + request.getGameId());
        }

        // Sprawdź, czy twórca harmonogramu to GameMaster
        if (!gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono gry o id: " + request.getGameId()))
                .getOwner().getId().equals(creator.getId())) {
            throw new IllegalStateException("Tylko GameMaster może stworzyć harmonogram");
        }

        // Sprawdź, czy gra ma status ACTIVE
        if (gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono gry o id: " + request.getGameId()))
                .getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra musi być aktywna, aby stworzyć dla niej harmonogram.");
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
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono gry o id: " + request.getGameId())));

        scheduler.setCreator(userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono twórcy gry: " + request.getCreatorId())));

        // Zakresy dat
        scheduler.setDateRanges(
                request.getDateRanges().stream()
                        .map(dto -> new SchedulerDateRange(
                                null,
                                dto.getStartDate(),
                                dto.getEndDate(),
                                scheduler
                        ))
                        .collect(Collectors.toList())
        );

        // Zakresy czasowe
        scheduler.setTimeRanges(
                request.getTimeRanges().stream()
                        .map(dto -> new TimeRange(
                                dto.getStartTime(),
                                dto.getEndTime()
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
                                    .orElseThrow(() -> new IllegalStateException("Nie znaleziono gracza: " + pid)));
                            participant.setNotifiedByEmail(true);
                            return participant;
                        })
                        .collect(Collectors.toList())
        );

        // Zapisz scheduler
        Scheduler saved = schedulerRepository.save(scheduler);

        emailService.sendSchedulerCreatedNotification(saved);

        // Mapowanie do DTO
        return SchedulerResponseMapper.mapToDto(saved);
    }

    /**
     * Validates the given request for creating a scheduler.
     * @param request The request to validate.
     * @throws IllegalStateException If the request is invalid.
     */
    private void validateRequest(CreateSchedulerRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalStateException("Tytuł nie może być pusty");
        }

        if (request.getDeadline() == null || request.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Termin wypełnienia dostępności nie może być w przeszłości");
        }

        if (request.getMinimumSessionDurationMinutes() == null || request.getMinimumSessionDurationMinutes() <= 0) {
            throw new IllegalStateException("Minimalny czas sesji musi być większy od 0");
        }

        if (request.getDateRanges() == null || request.getDateRanges().isEmpty()) {
            throw new IllegalStateException("Musi być podany przynajmniej jeden zakres dat");
        }

        for (CreateSchedulerRequest.DateRangeDto range : request.getDateRanges()) {
            if (range.getStartDate() == null || range.getEndDate() == null) {
                throw new IllegalStateException("Zakresy dat nie mogą być puste");
            }

            if (range.getStartDate().isAfter(range.getEndDate())) {
                throw new IllegalStateException("Data początkowa nie może być po dacie końcowej");
            }

            if (range.getEndDate().isBefore(LocalDate.now())) {
                throw new IllegalStateException("Data końcowa nie może być w przeszłości");
            }
        }

        if (request.getTimeRanges() == null || request.getTimeRanges().isEmpty()) {
            throw new IllegalStateException("Musi być podany przynajmniej jeden zakres godzin");
        }

        for (TimeRangeDto timeRange : request.getTimeRanges()) {
            if (timeRange.getStartTime() == null || timeRange.getEndTime() == null) {
                throw new IllegalStateException("Zakresy godzin nie mogą być puste");
            }

            if (timeRange.getStartTime().isAfter(timeRange.getEndTime())) {
                throw new IllegalStateException("Godzina początkowa nie może być po godzinie końcowej");
            }
        }

        if (request.getParticipantIds() == null || request.getParticipantIds().isEmpty()) {
            throw new IllegalStateException("Musi być przynajmniej jeden uczestnik");
        }

        long uniqueCount = request.getParticipantIds().stream().distinct().count();
        if (uniqueCount != request.getParticipantIds().size()) {
            throw new IllegalStateException("Na liście uczestników znajdują się duplikaty");
        }

        if (!request.getParticipantIds().contains(request.getCreatorId())) {
            throw new IllegalStateException("Twórca harmonogramu powinien być również jego uczestnikiem");
        }

        for (Long participantId : request.getParticipantIds()) {
            if (!this.gameUsersRepository.existsByGameIdAndUserId(request.getGameId(), participantId)) {
                throw new IllegalStateException("Uczestnik o id " + participantId + " nie jest uczestnikiem gry");
            }
        }
    }

    /**
     * Downloads all schedulers for a given game.
     *
     * @param gameId The ID of the game for which to download schedulers.
     * @param currentUser The user who is requesting the schedulers.
     * @return A list of schedulers as {@link SchedulerResponse} objects.
     * @throws IllegalStateException If the user does not have access to the game or if the game is not found.
     */
    public List<SchedulerResponse> getSchedulersByGame(Long gameId, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        if (!gameUsersRepository.existsByGameIdAndUserId(gameId, user.getId())) {
            throw new IllegalStateException("Nie masz dostępu do tego harmonogramu");
        }

        return schedulerRepository.findByGameId(gameId).stream()
                .map(SchedulerResponseMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets a scheduler by its ID.
     *
     * @param schedulerId The ID of the scheduler to retrieve.
     * @param currentUser The user who is requesting the scheduler.
     * @return The scheduler as a {@link SchedulerResponse}.
     * @throws IllegalStateException If the user does not have access to the scheduler
     * @throws ResourceNotFoundException If the scheduler is not found or the user is not found.
     */
    public SchedulerResponse getSchedulerById(Long schedulerId, @AuthenticationPrincipal User currentUser) {

        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!gameUsersRepository.existsByGameIdAndUserId(scheduler.getGame().getId(), user.getId())) {
            throw new IllegalStateException("Nie masz dostępu do tego harmonogramu");
        }

        return SchedulerResponseMapper.mapToDto(scheduler);
    }

    /**
     * Edits an existing scheduler based on the provided request and user currentUser.
     *
     * @param request   The request containing the updated details of the scheduler.
     * @param currentUser The user who is editing the scheduler.
     * @return The updated scheduler as a {@link SchedulerResponse}.
     * @throws IllegalStateException If the user is not the creator of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public SchedulerResponse editScheduler(EditSchedulerRequest request, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + request.getSchedulerId()));

        if (!scheduler.getCreator().getId().equals(user.getId())) {
            throw new IllegalStateException("Tylko twórca harmonogramu może go edytować");
        }

        if (scheduler.getFinalDecision() != null) {
            throw new IllegalStateException("Nie można edytować harmonogramu po wybraniu terminu");
        }

        validateEditRequest(request, scheduler);

        scheduler.setTitle(request.getTitle());
        scheduler.setDeadline(request.getDeadline());
        scheduler.setMinimumSessionDurationMinutes(request.getMinimumSessionDurationMinutes());

        scheduler.getDateRanges().clear();
        scheduler.getDateRanges().addAll(
                request.getDateRanges().stream()
                        .map(dto -> new SchedulerDateRange(
                                null,
                                dto.getStartDate(),
                                dto.getEndDate(),
                                scheduler
                        ))
                        .toList()
        );

        scheduler.getTimeRanges().clear();
        scheduler.getTimeRanges().addAll(
                request.getTimeRanges().stream()
                        .map(dto -> new TimeRange(
                                dto.getStartTime(),
                                dto.getEndTime()
                        ))
                        .toList()
        );

        scheduler.getParticipants().removeIf(participant ->
                !request.getParticipantIds().contains(participant.getPlayer().getId()));

        for (Long participantId : request.getParticipantIds()) {
            boolean exists = scheduler.getParticipants().stream()
                    .anyMatch(p -> p.getPlayer().getId().equals(participantId));

            if (!exists) {
                User participantUser = userRepository.findById(participantId)
                        .orElseThrow(() -> new IllegalStateException("Nie znaleziono uczestnika o ID: " + participantId));

                SchedulerParticipant participant = new SchedulerParticipant();
                participant.setPlayer(participantUser);
                participant.setScheduler(scheduler);
                participant.setNotifiedByEmail(true);
                scheduler.getParticipants().add(participant);
            }
        }

        for (SchedulerParticipant participant : scheduler.getParticipants()) {
            if (participant.getAvailabilitySlots() == null || participant.getAvailabilitySlots().isEmpty()) {
                continue;
            }

            List<AvailabilitySlot> validSlots = new ArrayList<>();

            for (AvailabilitySlot slot : participant.getAvailabilitySlots()) {
                LocalDateTime originalStart = slot.getStartDateTime();
                LocalDateTime originalEnd = slot.getEndDateTime();

                Optional<SchedulerDateRange> matchingDateRange = scheduler.getDateRanges().stream()
                        .filter(dr -> !(originalEnd.toLocalDate().isBefore(dr.getStartDate()) ||
                                originalStart.toLocalDate().isAfter(dr.getEndDate())))
                        .findFirst();

                Optional<TimeRange> matchingTimeRange = scheduler.getTimeRanges().stream()
                        .filter(tr -> !(originalEnd.toLocalTime().isBefore(tr.getStartTime()) ||
                                originalStart.toLocalTime().isAfter(tr.getEndTime())))
                        .findFirst();

                if (matchingDateRange.isPresent() && matchingTimeRange.isPresent()) {
                    LocalDateTime clippedStart = originalStart;
                    LocalDateTime clippedEnd = originalEnd;

                    if (clippedStart.toLocalTime().isBefore(matchingTimeRange.get().getStartTime())) {
                        clippedStart = LocalDateTime.of(clippedStart.toLocalDate(), matchingTimeRange.get().getStartTime());
                    }
                    if (clippedEnd.toLocalTime().isAfter(matchingTimeRange.get().getEndTime())) {
                        clippedEnd = LocalDateTime.of(clippedEnd.toLocalDate(), matchingTimeRange.get().getEndTime());
                    }

                    if (clippedStart.toLocalDate().isBefore(matchingDateRange.get().getStartDate())) {
                        clippedStart = LocalDateTime.of(matchingDateRange.get().getStartDate(), clippedStart.toLocalTime());
                    }
                    if (clippedEnd.toLocalDate().isAfter(matchingDateRange.get().getEndDate())) {
                        clippedEnd = LocalDateTime.of(matchingDateRange.get().getEndDate(), clippedEnd.toLocalTime());
                    }

                    if (clippedEnd.isAfter(clippedStart)) {
                        AvailabilitySlot newSlot = new AvailabilitySlot();
                        newSlot.setStartDateTime(clippedStart);
                        newSlot.setEndDateTime(clippedEnd);
                        newSlot.setAvailabilityType(slot.getAvailabilityType());
                        newSlot.setParticipant(participant);
                        validSlots.add(newSlot);
                    }
                }
            }
            participant.getAvailabilitySlots().clear();
            participant.getAvailabilitySlots().addAll(validSlots);
        }

        Scheduler updated = schedulerRepository.save(scheduler);
        return SchedulerResponseMapper.mapToDto(updated);
    }

    /**
     * Validates the edit request for a scheduler.
     * @param request The request to validate.
     * @param scheduler The scheduler to validate against.
     * @throws IllegalStateException If the request is invalid.
     */
    private void validateEditRequest(EditSchedulerRequest request, Scheduler scheduler) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalStateException("Tytuł nie może być pusty");
        }

        if (request.getDeadline() == null || request.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Termin wypełnienia dostępności nie może być w przeszłości");
        }

        if (request.getMinimumSessionDurationMinutes() == null || request.getMinimumSessionDurationMinutes() <= 0) {
            throw new IllegalStateException("Minimalny czas sesji musi być większy od 0");
        }

        if (request.getDateRanges() == null || request.getDateRanges().isEmpty()) {
            throw new IllegalStateException("Musi być podany przynajmniej jeden zakres dat");
        }

        for (EditSchedulerRequest.DateRangeDto range : request.getDateRanges()) {
            if (range.getStartDate() == null || range.getEndDate() == null) {
                throw new IllegalStateException("Zakresy dat nie mogą być puste");
            }

            if (range.getStartDate().isAfter(range.getEndDate())) {
                throw new IllegalStateException("Data początkowa nie może być po dacie końcowej");
            }

            if (range.getEndDate().isBefore(LocalDate.now())) {
                throw new IllegalStateException("Data końcowa nie może być w przeszłości");
            }
        }

        if (request.getTimeRanges() == null || request.getTimeRanges().isEmpty()) {
            throw new IllegalStateException("Musi być podany przynajmniej jeden zakres godzin");
        }

        for (TimeRangeDto timeRange : request.getTimeRanges()) {
            if (timeRange.getStartTime() == null || timeRange.getEndTime() == null) {
                throw new IllegalStateException("Zakresy godzin nie mogą być puste");
            }

            if (timeRange.getStartTime().isAfter(timeRange.getEndTime())) {
                throw new IllegalStateException("Godzina początkowa nie może być po godzinie końcowej");
            }
        }

        if (request.getParticipantIds() == null || request.getParticipantIds().isEmpty()) {
            throw new IllegalStateException("Musi być przynajmniej jeden uczestnik");
        }

        long uniqueCount = request.getParticipantIds().stream().distinct().count();
        if (uniqueCount != request.getParticipantIds().size()) {
            throw new IllegalStateException("Na liście uczestników znajdują się duplikaty");
        }

        // Sprawdź, czy twórca harmonogramu jest na liście uczestników
        if (!request.getParticipantIds().contains(scheduler.getCreator().getId())) {
            throw new IllegalStateException("Twórca harmonogramu powinien być również jego uczestnikiem");
        }

        // Sprawdź, czy wszyscy uczestnicy są w grze
        for (Long participantId : request.getParticipantIds()) {
            if (!gameUsersRepository.existsByGameIdAndUserId(scheduler.getGame().getId(), participantId)) {
                throw new IllegalStateException("Uczestnik o id " + participantId + " nie jest uczestnikiem gry");
            }
        }
    }


    /**
     * Deletes a scheduler by its ID.
     *
     * @param schedulerId The ID of the scheduler to delete.
     * @param currentUser The user who is submitting the final decision.
     * @throws IllegalStateException If the user is not the creator of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public void deleteScheduler(Long schedulerId, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!scheduler.getCreator().getId().equals(user.getId())) {
            throw new IllegalStateException("Tylko twórca harmonogramu może go usunąć");
        }

        schedulerRepository.delete(scheduler);
    }

    /**
     * Sets the final decision for a given scheduler.
     *
     * @param request   The request containing the final decision details.
     * @param currentUser The user who is submitting the final decision.
     * @return The updated scheduler as a {@link SchedulerResponse}.
     * @throws IllegalStateException If the user is not the creator of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public SchedulerResponse setFinalDecision(SetFinalDecisionRequest request, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + request.getSchedulerId()));

        if (!scheduler.getCreator().getId().equals(user.getId())) {
            throw new IllegalStateException("Tylko twórca harmonogramu może ustawić ostateczny termin");
        }

        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        if (start == null || end == null) {
            throw new IllegalStateException("Daty nie mogą być puste");
        }

        if (start.isAfter(end)) {
            throw new IllegalStateException("Data rozpoczęcia nie może być po dacie zakończenia");
        }

        long minutes = Duration.between(start, end).toMinutes();
        if (minutes < scheduler.getMinimumSessionDurationMinutes()) {
            throw new IllegalStateException("Wybrany slot jest zbyt krótki");
        }

        // WALIDACJA: Czy mieści się w którymkolwiek slocie z dostępności uczestników?
        boolean isCovered = scheduler.getParticipants().stream()
                .flatMap(p -> p.getAvailabilitySlots().stream())
                .filter(s -> s.getAvailabilityType() != AvailabilityType.NO)
                .anyMatch(s -> !s.getStartDateTime().isAfter(start) &&
                        !s.getEndDateTime().isBefore(end));

        if (!isCovered) {
            throw new IllegalStateException("Żaden uczestnik nie jest dostępny w tym przedziale");
        }

        FinalDecision decision = new FinalDecision();
        decision.setStart(start);
        decision.setEnd(end);

        scheduler.setFinalDecision(decision);
        scheduler.setStatus(SchedulerStatus.FINALIZED);

        // Link do Google Calendar
        String title = URLEncoder.encode(scheduler.getTitle(), StandardCharsets.UTF_8);
        String details = URLEncoder.encode("Spotkanie RPG przez aplikację RPG Handy Helper", StandardCharsets.UTF_8);

        String startUtc = start.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));

        String endUtc = end.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));

        String calendarLink = String.format(
                "https://calendar.google.com/calendar/render?action=TEMPLATE&text=%s&dates=%s/%s&details=%s",
                title, startUtc, endUtc, details
        );

        scheduler.setGoogleCalendarLink(calendarLink);

        Scheduler saved = schedulerRepository.save(scheduler);
        return SchedulerResponseMapper.mapToDto(saved);
    }


    /**
     * Sets the availability for a given scheduler.
     *
     * @param request   The request containing the availability slots.
     * @param currentUser The user who is submitting the availability.
     * @throws IllegalStateException If the user is not a participant of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public void submitAvailability(SubmitAvailabilityRequest request, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + request.getSchedulerId()));

        // Sprawdź, czy harmonogram nie został już sfinalizowany
        if (scheduler.getFinalDecision() != null) {
            throw new IllegalStateException("Nie można zmienić dostępności po wybraniu finalnego terminu");
        }

        // Walidacja slotów
        validateAvailabilitySlots(request.getSlots(), scheduler);

        // Sprawdź, czy użytkownik jest uczestnikiem harmonogramu
        SchedulerParticipant participant = scheduler.getParticipants().stream()
                .filter(p -> p.getPlayer().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Użytkownik nie jest uczestnikiem tego harmonogramu"));

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

        // Po zaktualizowaniu dostępności danego gracza:
        boolean allSubmitted = scheduler.getParticipants().stream()
                .allMatch(p -> p.getAvailabilitySlots() != null && !p.getAvailabilitySlots().isEmpty());

        if (allSubmitted) {
            scheduler.setStatus(SchedulerStatus.READY_TO_DECIDE);
        } else {
            scheduler.setStatus(SchedulerStatus.AWAITING_AVAILABILITY);
        }

        // Zapisz zmiany
        schedulerRepository.save(scheduler);
    }

    /**
     * Validates availability slots to ensure they are correct.
     *
     * @param slots The slots to validate.
     * @throws IllegalStateException If any validation fails.
     */
    private void validateAvailabilitySlots(List<SubmitAvailabilityRequest.AvailabilitySlotDto> slots, Scheduler scheduler) {
        if (slots == null || slots.isEmpty()) {
            throw new IllegalStateException("Lista dostępności nie może być pusta");
        }

        // Sprawdzenie, czy każdy slot mieści się w zakresach dat i godzin schedulera
        for (SubmitAvailabilityRequest.AvailabilitySlotDto slot : slots) {
            if (slot.getStartDateTime() == null || slot.getEndDateTime() == null) {
                throw new IllegalStateException("Daty początku i końca slotu nie mogą być puste");
            }

            if (slot.getStartDateTime().isAfter(slot.getEndDateTime())) {
                throw new IllegalStateException("Data rozpoczęcia nie może być po dacie zakończenia");
            }

            if (slot.getAvailabilityType() == null) {
                throw new IllegalStateException("Typ dostępności nie może być pusty");
            }

            // Walidacja czy slot mieści się w zakresach dat schedulera
            LocalDate slotDate = slot.getStartDateTime().toLocalDate();
            LocalTime slotStartTime = slot.getStartDateTime().toLocalTime();
            LocalTime slotEndTime = slot.getEndDateTime().toLocalTime();

            // Sprawdź, czy data mieści się w którymkolwiek zakresie dat
            boolean dateInRange = scheduler.getDateRanges().stream()
                    .anyMatch(dr ->
                            (slotDate.isEqual(dr.getStartDate()) || slotDate.isAfter(dr.getStartDate()))
                                    && (slotDate.isEqual(dr.getEndDate()) || slotDate.isBefore(dr.getEndDate()))
                    );

            if (!dateInRange) {
                throw new IllegalStateException("Slot o dacie " + slotDate + " nie mieści się w żadnym z zakresów dat harmonogramu");
            }

            // Sprawdź, czy godziny mieszczą się w którymkolwiek zakresie godzin
            boolean timeInRange = scheduler.getTimeRanges().stream()
                    .anyMatch(tr ->
                            (slotStartTime.equals(tr.getStartTime()) || slotStartTime.isAfter(tr.getStartTime()))
                                    && (slotEndTime.equals(tr.getEndTime()) || slotEndTime.isBefore(tr.getEndTime()))
                    );

            if (!timeInRange) {
                throw new IllegalStateException("Slot o godzinach " + slotStartTime + " - " + slotEndTime
                        + " nie mieści się w żadnym z zakresów godzin harmonogramu");
            }
        }

        // Sprawdzanie nakładania się slotów
        List<SubmitAvailabilityRequest.AvailabilitySlotDto> sortedSlots = slots.stream()
                .sorted(Comparator.comparing(SubmitAvailabilityRequest.AvailabilitySlotDto::getStartDateTime))
                .toList();

        for (int i = 0; i < sortedSlots.size() - 1; i++) {
            if (sortedSlots.get(i).getEndDateTime().isAfter(sortedSlots.get(i+1).getStartDateTime())) {
                throw new IllegalStateException("Sloty dostępności nie mogą na siebie nachodzić");
            }
        }
    }

    /**
     * Gets the availability of a player for a given scheduler.
     *
     * @param schedulerId The ID of the scheduler.
     * @param currentUser   The user who is requesting the availability.
     * @return {@link PlayerAvailabilityResponse} The availability response containing the player's availability slots.
     * @throws IllegalStateException If the user is not a participant of the scheduler or if the scheduler is not found.
     */
    @Transactional(readOnly = true)
    public PlayerAvailabilityResponse getPlayerAvailability(Long schedulerId, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika " + currentUser.getUsername()));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + schedulerId));

        SchedulerParticipant participant = scheduler.getParticipants().stream()
                .filter(p -> p.getPlayer().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Użytkownik nie jest uczestnikiem tego harmonogramu"));

        List<PlayerAvailabilityResponse.AvailabilitySlotDto> slots = participant.getAvailabilitySlots().stream()
                .map(s -> new PlayerAvailabilityResponse.AvailabilitySlotDto(
                        s.getStartDateTime(),
                        s.getEndDateTime(),
                        s.getAvailabilityType()
                )).toList();

        return new PlayerAvailabilityResponse(user.getId(), slots);
    }

    /**
     * Suggests time slots based on the availability of participants in a given scheduler.
     *
     * @param schedulerId The ID of the scheduler.
     * @param currentUser   The user who is requesting the suggested slots.
     * @return {@link SuggestedSlotResponse} The suggested time slots.
     * @throws IllegalStateException If the user does not have access to the scheduler or if the scheduler is not found.
     */
    @Transactional(readOnly = true)
    public SuggestedSlotResponse suggestTimeSlots(Long schedulerId, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!gameUsersRepository.existsByGameIdAndUserId(scheduler.getGame().getId(), user.getId())) {
            throw new IllegalStateException("Nie masz dostępu do tego harmonogramu");
        }

        int minDuration = scheduler.getMinimumSessionDurationMinutes();
        int participantsCount = scheduler.getParticipants().size();

        // Dynamiczny próg — dla małych grup (1-2 osoby) obniżamy wymaganie
        double threshold = Math.min(1.5, participantsCount * 0.6);

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
                    case AvailabilityType.NO -> -1.0;
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
            double previousWeight = currentWeight;
            currentWeight += point.delta;

            boolean isWindowStart = (windowStart == null && currentWeight >= threshold);
            boolean isWindowEnd = (windowStart != null && currentWeight < threshold);

            if (isWindowStart) {
                windowStart = point.time;
            }

            if (isWindowEnd) {
                long minutes = Duration.between(windowStart, point.time).toMinutes();
                if (minutes >= minDuration) {
                    suggested.add(new SuggestedSlotResponse.TimeSlotDto(
                            windowStart,
                            point.time,
                            previousWeight // Używamy wagi przed zmianą punktu końcowego
                    ));
                }
                windowStart = null;
            }
        }

        // Sprawdź, czy nie mamy niezamkniętego przedziału na końcu
        if (windowStart != null && currentWeight >= threshold) {
            LocalDateTime lastTime = timeline.getLast().time;
            if (lastTime != null) {
                long minutes = Duration.between(windowStart, lastTime).toMinutes();
                if (minutes >= minDuration) {
                    suggested.add(new SuggestedSlotResponse.TimeSlotDto(
                            windowStart,
                            lastTime,
                            currentWeight
                    ));
                }
            }
        }

        suggested.sort(Comparator.comparingDouble(SuggestedSlotResponse.TimeSlotDto::getWeightOfTimeSlot).reversed());

        return new SuggestedSlotResponse(suggested);
    }

    /**
     * Merges user slots to remove gaps and combine adjacent slots of the same type.
     *
     * @param slots The list of availability slots to merge.
     * @return A list of {@link AvailabilitySlot}.
     */
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

    /**
     * Sends final decision emails to all participants of a given scheduler.
     *
     * @param schedulerId The ID of the scheduler.
     * @param currentUser   The user who is sending the emails.
     * @throws IllegalStateException If the user is not the creator of the scheduler or if the scheduler is not found.
     */
    @Transactional
    public void sendFinalDecisionMails(Long schedulerId, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!scheduler.getCreator().getId().equals(user.getId())) {
            throw new IllegalStateException("Tylko twórca może wysłać maile");
        }

        if (scheduler.getFinalDecision() == null) {
            throw new IllegalStateException("Nie można wysłać maili bez wybrania terminu");
        }

        if (scheduler.isEmailsSent()) {
            throw new IllegalArgumentException("Maile z potwierdzeniem zostały już wysłane");
        }

        scheduler.setEmailsSent(true);
        emailService.sendFinalDecisionNotification(scheduler);
        schedulerRepository.save(scheduler);
    }

    /**
     * Edits the availability of a player for a given scheduler.
     *
     * @param request   The request containing the updated availability slots.
     * @param currentUser The user who is editing the availability.
     * @throws IllegalStateException If the user is not a participant of the scheduler or if the scheduler is not found.
     */
    public void editAvailability(SubmitAvailabilityRequest request, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + request.getSchedulerId()));

        // Sprawdź, czy harmonogram nie został już sfinalizowany
        if (scheduler.getFinalDecision() != null) {
            throw new IllegalStateException("Nie można zmienić dostępności po wybraniu finalnego terminu");
        }

        // Walidacja slotów
        validateAvailabilitySlots(request.getSlots(), scheduler);

        // Sprawdź, czy użytkownik jest uczestnikiem harmonogramu
        SchedulerParticipant participant = scheduler.getParticipants().stream()
                .filter(p -> p.getPlayer().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Użytkownik nie jest uczestnikiem tego harmonogramu"));

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

        // Po zaktualizowaniu dostępności danego gracza:
        boolean allSubmitted = scheduler.getParticipants().stream()
                .allMatch(p -> p.getAvailabilitySlots() != null && !p.getAvailabilitySlots().isEmpty());

        if (allSubmitted) {
            scheduler.setStatus(SchedulerStatus.READY_TO_DECIDE);
        } else {
            scheduler.setStatus(SchedulerStatus.AWAITING_AVAILABILITY);
        }

        // Zapisz zmiany
        schedulerRepository.save(scheduler);
    }

    /**
     * Gets the availability of all players for a given scheduler.
     *
     * @param schedulerId The ID of the scheduler.
     * @param currentUser   The user who is requesting the availability.
     * @return A list of {@link PlayerAvailabilityResponse} containing the availability of each player.
     * @throws IllegalStateException If the user does not have access to the scheduler or if the scheduler is not found.
     */
    public List<PlayerAvailabilityResponse> getAllPlayerAvailability(Long schedulerId, User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika " + currentUser.getUsername()));

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono harmonogramu o id: " + schedulerId));

        if (!gameUsersRepository.existsByGameIdAndUserId(scheduler.getGame().getId(), user.getId())) {
            throw new IllegalStateException("Nie masz dostępu do tego harmonogramu");
        }

        List<PlayerAvailabilityResponse> responses = new ArrayList<>();
        for (SchedulerParticipant participant : scheduler.getParticipants()) {
            PlayerAvailabilityResponse response = new PlayerAvailabilityResponse();
            response.setPlayerId(participant.getPlayer().getId());
            response.setAvailability(participant.getAvailabilitySlots().stream()
                    .map(slot -> new PlayerAvailabilityResponse.AvailabilitySlotDto(
                            slot.getStartDateTime(),
                            slot.getEndDateTime(),
                            slot.getAvailabilityType()
                    )).toList());
            responses.add(response);
        }
        return responses;
    }

    /**
     * Gets future final decisions for a game.
     *
     * @param loggedUser The user who is logged in.
     * @param gameId The ID of the game.
     * @return A map containing future final decisions for the game.
     * @throws IllegalStateException If the user does not have access to the game or if the user is not found.
     */
    public Map<String, Object> getFutureFinalDecisionsForGame(User loggedUser, Long gameId) {
        User user = userRepository.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        if (!gameUsersRepository.existsByGameIdAndUserId(gameId, user.getId())) {
            throw new IllegalStateException("Nie masz dostępu do tej gry");
        }

        List<Scheduler> schedulers = schedulerRepository.findAllByGameIdAndStatus(gameId, SchedulerStatus.FINALIZED);

        if (schedulers.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Nie znaleziono przyszłych decyzji harmonogramów dla tej gry.");
        }

        List<SchedulerResponse> responses = schedulers.stream()
                .filter(scheduler -> scheduler.getFinalDecision() != null &&
                        scheduler.getFinalDecision().getStart().isAfter(LocalDateTime.now()))
                .map(SchedulerResponseMapper::mapToDto)
                .toList();

        List<SchedulerResponse.FinalDecisionDto> finalDecisions = responses.stream()
                .map(SchedulerResponse::getFinalDecision)
                .filter(Objects::nonNull)
                .toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano przyszłe decyzje harmonogramów dla gry.");
        response.put("futureGames", finalDecisions);
        return response;
    }

    /**
     * Gets all pending schedulers for the current user.
     *
     * @param currentUser The currently logged-in user.
     * @return A list of {@link SchedulerResponse} containing pending schedulers.
     * @throws IllegalStateException If the user is not found.
     */
    public List<SchedulerResponse> getPendingSchedulers(User currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        List<Scheduler> schedulers = schedulerRepository.findAllBySchedulerParticipantId(user.getId());

        return schedulers.stream()
                .filter(scheduler -> scheduler.getParticipants().stream()
                        .anyMatch(participant -> participant.getPlayer().getId().equals(user.getId()) &&
                                (participant.getAvailabilitySlots() == null || participant.getAvailabilitySlots().isEmpty())))
                .map(SchedulerResponseMapper::mapToDto)
                .toList();
    }

    /**
     * Gets the next session for a given game.
     *
     * @param currentUser The currently logged-in user.
     * @param gameId The ID of the game.
     * @return A map containing the name and date of the next session.
     * @throws IllegalStateException If the user does not have access to the game or if the user is not found.
     */
    public Map<String, Object> getNextSession(User currentUser, Long gameId) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        if (!gameUsersRepository.existsByGameIdAndUserId(gameId, user.getId())) {
            throw new IllegalStateException("Nie masz dostępu do tej gry");
        }

        List<Scheduler> schedulers = schedulerRepository.findAllByGameIdAndStatus(gameId, SchedulerStatus.FINALIZED);

        Scheduler nextSession = schedulers.stream()
                .filter(scheduler -> scheduler.getFinalDecision() != null &&
                        scheduler.getFinalDecision().getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(s -> s.getFinalDecision().getStart()))
                .orElse(null);

        if (nextSession == null) {
            return CustomReturnables.getOkResponseMap("Brak nadchodzących sesji.");
        }

        NextSession nextSessionDto = new NextSession();
        nextSessionDto.setSessionName(nextSession.getTitle());
        nextSessionDto.setGameName(nextSession.getGame().getName());
        nextSessionDto.setStartTime(nextSession.getFinalDecision().getStart().toLocalTime());

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano najbliższą sesję.");
        response.put("nextSession", nextSessionDto);
        return response;
    }

    /**
     * A helper class to represent a time point on the timeline with its weight (delta).
     */
    private static class TimePoint {
        LocalDateTime time;
        double delta;

        public TimePoint(LocalDateTime time, double delta) {
            this.time = time;
            this.delta = delta;
        }
    }
}