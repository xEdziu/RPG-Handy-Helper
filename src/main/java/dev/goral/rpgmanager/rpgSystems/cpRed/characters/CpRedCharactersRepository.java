package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CpRedCharactersRepository extends JpaRepository<CpRedCharacters, Long> {

    List<CpRedCharacters> findAll();
    boolean existsByGameIdAndName(Long gameId, String name);

    @Query("SELECT COUNT(c) FROM CpRedCharacters c WHERE c.user.id = :userId AND c.game.id = :gameId")
    Long countByUserIdAndGameId(@Param("userId") Long userId, @Param("gameId") Long gameId);

}
