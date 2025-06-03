package dev.goral.rpghandyhelper.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameNoteRepository extends JpaRepository<GameNote, Long> {
    List<GameNote> findByUserId(Long userId);
    List<GameNote> findByGameIdAndUserId(Long gameId, Long userId);
    Optional<GameNote> findByUserIdAndTitleIgnoreCaseAndGameId(Long userId, String title, Long gameId);
}
