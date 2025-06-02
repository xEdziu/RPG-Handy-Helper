package dev.goral.rpghandyhelper.chat;

import dev.goral.rpghandyhelper.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRoomHistoryRepository extends JpaRepository<GameRoomHistory, Long> {
    List<GameRoomHistory> findByParticipantsContaining(User user);
}
