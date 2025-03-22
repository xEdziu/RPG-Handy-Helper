package dev.goral.rpgmanager.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameNoteRepository extends JpaRepository<GameNote, Long> {
    List<GameNote> findByUserId(Long userId);
    List<GameNote> findByGameIdAndUserId(Long gameId, Long userId);
}
