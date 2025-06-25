package dev.goral.rpghandyhelper.scheduler.repository;

import dev.goral.rpghandyhelper.scheduler.entity.Scheduler;
import dev.goral.rpghandyhelper.scheduler.enums.SchedulerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
    List<Scheduler> findByGameId(Long gameId);
    List<Scheduler> findAllByGameIdAndStatus(Long gameId, SchedulerStatus schedulerStatus);
    @Query("SELECT s FROM Scheduler s JOIN s.participants p WHERE p.player.id = :participantId")
    List<Scheduler> findAllBySchedulerParticipantId(@Param("participantId") Long participantId);
}
