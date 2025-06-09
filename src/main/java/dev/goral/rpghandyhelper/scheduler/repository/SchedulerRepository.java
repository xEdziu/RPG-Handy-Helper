package dev.goral.rpghandyhelper.scheduler.repository;

import dev.goral.rpghandyhelper.scheduler.entity.Scheduler;
import dev.goral.rpghandyhelper.scheduler.enums.SchedulerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
    List<Scheduler> findByGameId(Long gameId);
    List<Scheduler> findAllByGameIdAndStatus(Long gameId, SchedulerStatus schedulerStatus);
}
