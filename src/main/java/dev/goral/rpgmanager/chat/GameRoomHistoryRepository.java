package dev.goral.rpgmanager.chat;

import dev.goral.rpgmanager.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRoomHistoryRepository extends JpaRepository<GameRoomHistory, Long> {
    List<GameRoomHistory> findByParticipantsContaining(User user);
}
