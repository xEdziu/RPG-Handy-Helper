package dev.goral.rpgmanager.scheduler.service;

import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.scheduler.dto.request.CreateSchedulerRequest;
import dev.goral.rpgmanager.scheduler.dto.request.SetFinalDecisionRequest;
import dev.goral.rpgmanager.scheduler.dto.response.SchedulerResponse;
import dev.goral.rpgmanager.scheduler.entity.*;
import dev.goral.rpgmanager.scheduler.repository.SchedulerRepository;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public SchedulerResponse setFinalDecision(SetFinalDecisionRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        Scheduler scheduler = schedulerRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono harmonogramu o id: " + request.getSchedulerId()));

        if (!scheduler.getCreator().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Tylko twórca harmonogramu może ustawić ostateczny termin");
        }

        // Walidacja dat
        if (request.getStart() == null || request.getEnd() == null) {
            throw new IllegalArgumentException("Daty nie mogą być puste");
        }
        if (request.getStart().isAfter(request.getEnd())) {
            throw new IllegalArgumentException("Data rozpoczęcia nie może być po dacie zakończenia");
        }

        FinalDecision decision = new FinalDecision();
        decision.setStart(request.getStart());
        decision.setEnd(request.getEnd());

        scheduler.setFinalDecision(decision);

        Scheduler saved = schedulerRepository.save(scheduler);
        return SchedulerResponseMapper.mapToDto(saved);
    }



}
