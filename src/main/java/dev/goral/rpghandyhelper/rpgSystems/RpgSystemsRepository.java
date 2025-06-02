package dev.goral.rpghandyhelper.rpgSystems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RpgSystemsRepository extends JpaRepository<RpgSystems, Long> {
    List<RpgSystems> findByName(String name);

    boolean existsByName(String name);
}
