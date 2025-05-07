package dev.goral.rpgmanager.scheduler.repository;

import dev.goral.rpgmanager.scheduler.entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
    List<Scheduler> findByGameId(Long gameId);
}
