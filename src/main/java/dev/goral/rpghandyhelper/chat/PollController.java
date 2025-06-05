package dev.goral.rpghandyhelper.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Kontroler obsługujący logikę ankiet w pokoju czatu.
 * <p>
 * Odbiera wiadomości STOMP od klientów na ścieżkach /app/poll/{roomId} (tworzenie ankiety),
 * /app/poll/{roomId}/vote (głosowanie), a także wysyła powiadomienia o stanie ankiety
 * (typ NEW, UPDATE, END) na temat /topic/poll/{roomId}.
 * </p>
 */
@Controller
@RequiredArgsConstructor
public class PollController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Mapa przechowująca w pamięci instancje aktywnych ankiet.
     * Klucz: pollId (unikalny identyfikator ankiety) → wartość: obiekt PollData.
     */
    private final Map<String, PollData> polls = new ConcurrentHashMap<>();

    /**
     * Scheduler do uruchamiania zakończenia ankiety po upływie czasu trwania.
     */
    private final static java.util.concurrent.ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Metoda wywoływana, gdy klient wysyła nowe żądanie tworzenia ankiety.
     * Oczekuje obiektu PollPayload zawierającego pytanie, opcje i czas trwania.
     * <p>
     * Po wygenerowaniu unikalnego pollId i obliczeniu expiresAt tworzy obiekt PollData,
     * zapisuje go w mapie polls, a następnie wysyła do wszystkich klientów w danym pokoju
     * powiadomienie typu NEW. Na koniec planuje zakończenie ankiety po upływie zadanego czasu.
     * </p>
     *
     * @param roomId    identyfikator pokoju czatu
     * @param payload   dane nowej ankiety (pytanie, lista opcji i czas trwania)
     * @param principal obiekt Principal wskazujący aktualnie zalogowanego użytkownika
     */
    @MessageMapping("/poll/{roomId}")
    public void createPoll(
            @DestinationVariable String roomId,
            @Payload PollPayload payload,
            Principal principal
    ) {
        String pollId = UUID.randomUUID().toString();
        long now = Instant.now().toEpochMilli();
        long expiresAt = now + payload.getDuration() * 1000L;

        PollData pollData = new PollData();
        pollData.setId(pollId);
        pollData.setQuestion(payload.getQuestion());
        pollData.setCreatedBy(principal.getName());
        pollData.setExpiresAt(expiresAt);
        pollData.setRoomId(roomId);

        List<OptionData> optionList = new ArrayList<>();
        for (String optText : payload.getOptions()) {
            optionList.add(new OptionData(optText));
        }
        pollData.setOptions(optionList);
        pollData.setVotedUsers(Collections.newSetFromMap(new ConcurrentHashMap<>()));

        polls.put(pollId, pollData);

        PollNotificationDTO dto = new PollNotificationDTO();
        dto.setType("NEW");
        dto.setId(pollId);
        dto.setQuestion(payload.getQuestion());
        dto.setOptions(payload.getOptions());
        dto.setCreatedBy(principal.getName());
        dto.setExpiresAt(expiresAt);

        messagingTemplate.convertAndSend("/topic/poll/" + roomId, dto);

        long delay = expiresAt - now;
        if (delay > 0) {
            scheduler.schedule(() -> endPoll(pollId), delay, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Metoda wywoływana, gdy klient oddaje głos w istniejącej ankiecie.
     * Oczekuje obiektu VotePayload zawierającego pollId, nazwę głosującego i indeks wybranej opcji.
     * <p>
     * Sprawdza, czy ankieta istnieje oraz, czy dany użytkownik jeszcze nie głosował.
     * Jeśli warunki są spełnione, zwiększa licznik głosów w odpowiedniej opcji i oznacza użytkownika jako zagłosowanego.
     * Następnie wysyła powiadomienie typu UPDATE do wszystkich klientów w danym pokoju.
     * </p>
     *
     * @param roomId      identyfikator pokoju czatu
     * @param votePayload dane głosowania (pollId, voter, optionIndex)
     * @param principal   obiekt Principal wskazujący aktualnie zalogowanego użytkownika
     */
    @MessageMapping("/poll/{roomId}/vote")
    public void vote(
            @DestinationVariable String roomId,
            @Payload VotePayload votePayload,
            Principal principal
    ) {
        String pollId = votePayload.getPollId();
        PollData pollData = polls.get(pollId);
        if (pollData == null) {
            return;
        }

        if (pollData.getVotedUsers().contains(principal.getName())) {
            return;
        }

        int idx = votePayload.getOptionIndex();
        if (idx < 0 || idx >= pollData.getOptions().size()) {
            return;
        }

        pollData.getOptions().get(idx).incrementVotes();
        pollData.getVotedUsers().add(principal.getName());

        UpdateNotificationDTO updateDto = new UpdateNotificationDTO();
        updateDto.setType("UPDATE");
        updateDto.setPollId(pollId);
        updateDto.setOptionIndex(idx);
        updateDto.setVotesForOption(pollData.getOptions().get(idx).getVotes());

        messagingTemplate.convertAndSend("/topic/poll/" + roomId, updateDto);
    }

    /**
     * Metoda wywołana po upływie czasu trwania ankiety, zaplanowana w schedulerze.
     * Pobiera PollData z pamięci, oblicza finalne wyniki, wysyła powiadomienie typu END
     * do wszystkich klientów w pokoju oraz systemową wiadomość na czat z podsumowaniem,
     * po czym usuwa ankietę z mapy polls.
     *
     * @param pollId unikalny identyfikator ankiety, która kończy swój czas trwania
     */
    private void endPoll(String pollId) {
        PollData pollData = polls.remove(pollId);
        if (pollData == null) {
            return;
        }
        String roomId = pollData.getRoomId();

        // 1) Budujemy listę finalnych wyników
        List<EndResultDTO> results = new ArrayList<>();
        List<OptionData> opts = pollData.getOptions();
        for (int i = 0; i < opts.size(); i++) {
            EndResultDTO r = new EndResultDTO();
            r.setOptionIndex(i);
            r.setVotes(opts.get(i).getVotes());
            results.add(r);
        }

        // 2) Emitujemy END → klienci wiedzą, że ankieta się zakończyła
        EndNotificationDTO endDto = new EndNotificationDTO();
        endDto.setType("END");
        endDto.setPollId(pollId);
        endDto.setFinalResults(results);

        messagingTemplate.convertAndSend("/topic/poll/" + roomId, endDto);

        // 3) Wysyłamy na kanał czatu systemową wiadomość z podsumowaniem ankiety
        StringBuilder sb = new StringBuilder();
        sb.append("Pytanie: **").append(pollData.getQuestion()).append("**\n");
        sb.append("Ostateczne wyniki:\n");
        for (OptionData opt : opts) {
            String optText = opt.getText();
            int votes = opt.getVotes();
            sb.append("• ").append(optText).append(": ").append(votes).append(" ")
                    .append(pluralizeVotes(votes)).append("\n");
        }
        if (results.stream().anyMatch(r -> r.getVotes() > 0)) {
            int maxVotes = results.stream().mapToInt(EndResultDTO::getVotes).max().orElse(0);
            List<EndResultDTO> winners = new ArrayList<>();
            for (EndResultDTO r : results) {
                if (r.getVotes() == maxVotes) {
                    winners.add(r);
                }
            }
            if (winners.size() == 1) {
                String winnerText = opts.get(winners.getFirst().getOptionIndex()).getText();
                sb.append("Zwycięzca to opcja **").append(winnerText).append("**");
            } else {
                sb.append("Brak jednoznacznego zwycięzcy, kilka opcji ma najwyższą liczbę głosów.");
            }
        } else {
            sb.append("Brak głosów, ankieta nie przyniosła żadnych wyników.");
        }

        Map<String, Object> chatMsg = new HashMap<>();
        chatMsg.put("from", "📊 Wynik Ankiety");
        chatMsg.put("content", sb.toString().trim());
        chatMsg.put("privateMessage", false);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMsg);
    }

    /**
     * Metoda pomocnicza do poprawnej odmiany słowa "głos" w zależności od liczby.
     *
     * @param count liczba głosów
     * @return odpowiednio odmieniona forma słowa "głos"
     */
    private String pluralizeVotes(int count) {
        if (count == 1) return "głos";
        if ((count % 10 >= 2 && count % 10 <= 4) && !(count % 100 >= 12 && count % 100 <= 14)) return "głosy";
        return "głosów";
    }

    // ——————————————————————————————————————————————
    //  K L A S Y   P O M O C N I C Z E
    // ——————————————————————————————————————————————

    /**
     * DTO przesyłany przez klienta przy tworzeniu ankiety.
     */
    @Data
    @NoArgsConstructor
    public static class PollPayload {
        /**
         * Pytanie ankiety.
         */
        private String question;

        /**
         * Lista tekstów opcji, na które można głosować.
         */
        private List<String> options;

        /**
         * Czas trwania ankiety w sekundach (30–120).
         */
        private int duration;
    }

    /**
     * DTO przesyłany przez klienta przy oddaniu głosu.
     */
    @Data
    @NoArgsConstructor
    public static class VotePayload {
        /**
         * Unikalny identyfikator ankiety, w której głosujemy.
         */
        private String pollId;

        /**
         * Nazwa użytkownika oddającego głos.
         */
        private String voter;

        /**
         * Indeks wybranej opcji (0-based).
         */
        private int optionIndex;
    }

    /**
     * Reprezentuje w pamięci stan pojedynczej ankiety.
     */
    @Data
    public static class PollData {
        /**
         * Unikalny identyfikator ankiety (UUID).
         */
        private String id;

        /**
         * Pytanie ankiety.
         */
        private String question;

        /**
         * Nazwa użytkownika, który utworzył ankietę.
         */
        private String createdBy;

        /**
         * Znacznik czasu (millis) wygaśnięcia ankiety.
         */
        private long expiresAt;

        /**
         * Identyfikator pokoju czatu, w którym ankieta jest prowadzona.
         */
        private String roomId;

        /**
         * Lista obiektów OptionData zawierających tekst opcji i liczbę głosów.
         */
        private List<OptionData> options;

        /**
         * Zbiór nazw użytkowników, którzy już oddali głos w tej ankiecie.
         */
        private Set<String> votedUsers;
    }

    /**
     * Reprezentuje pojedynczą opcję ankiety wraz z licznikiem oddanych głosów.
     */
    @Data
    @NoArgsConstructor
    public static class OptionData {
        /**
         * Tekst opcji (np. "Tak", "Nie", "Może").
         */
        private String text;

        /**
         * Liczba głosów oddanych na tę opcję.
         */
        private int votes = 0;

        /**
         * Tworzony konstruktor przyjmujący tekst opcji.
         *
         * @param text treść tej opcji
         */
        public OptionData(String text) {
            this.text = text;
        }

        /**
         * Zwiększa licznik głosów o 1.
         */
        public void incrementVotes() {
            this.votes++;
        }
    }

    /**
     * DTO rozsyłany do klientów przy utworzeniu nowej ankiety (typ NEW).
     */
    @Data
    @NoArgsConstructor
    public static class PollNotificationDTO {
        /**
         * Typ wiadomości: "NEW" oznacza nową ankietę.
         */
        private String type;

        /**
         * Unikalny identyfikator ankiety (UUID).
         */
        private String id;

        /**
         * Pytanie ankiety.
         */
        private String question;

        /**
         * Lista tekstów opcji.
         */
        private List<String> options;

        /**
         * Nazwa użytkownika, który utworzył ankietę.
         */
        private String createdBy;

        /**
         * Znacznik czasu (millis) wygaśnięcia ankiety.
         */
        private long expiresAt;
    }

    /**
     * DTO rozsyłany do klientów po każdym oddaniu głosu (typ UPDATE).
     */
    @Data
    @NoArgsConstructor
    public static class UpdateNotificationDTO {
        /**
         * Typ wiadomości: "UPDATE" oznacza aktualizację stanu głosowania.
         */
        private String type;

        /**
         * Unikalny identyfikator ankiety, która została zaktualizowana.
         */
        private String pollId;

        /**
         * Indeks opcji, w której zmieniono liczbę głosów.
         */
        private int optionIndex;

        /**
         * Bieżąca liczba głosów w danej opcji.
         */
        private int votesForOption;
    }

    /**
     * DTO rozsyłany do klientów po zakończeniu ankiety (typ END).
     */
    @Data
    @NoArgsConstructor
    public static class EndNotificationDTO {
        /**
         * Typ wiadomości: "END" oznacza zakończenie ankiety.
         */
        private String type;

        /**
         * Unikalny identyfikator ankiety, która się zakończyła.
         */
        private String pollId;

        /**
         * Lista wyników: dla każdej opcji indeks i liczba głosów.
         */
        private List<EndResultDTO> finalResults;
    }

    /**
     * DTO przedstawiający wynik pojedynczej opcji ankiety.
     */
    @Data
    @NoArgsConstructor
    public static class EndResultDTO {
        /**
         * Indeks opcji (0-based).
         */
        private int optionIndex;

        /**
         * Całkowita liczba głosów oddanych na tę opcję.
         */
        private int votes;
    }
}