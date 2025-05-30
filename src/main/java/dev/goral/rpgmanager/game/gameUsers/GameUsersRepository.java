package dev.goral.rpgmanager.game.gameUsers;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameUsersRepository extends JpaRepository<GameUsers, Long> {
    Optional<GameUsers> findByUserUsername(String username);
    Optional<GameUsers> findGameUsersByGame(Game game);
    List<GameUsers> findAllByUserId(Long id);

    Optional<GameUsers> findGameUsersByUserIdAndGameId(Long userId, Long gameId);

    List<GameUsers> findGameAllUsersByGameId(Long gameId);

    boolean existsByUserIdAndGameId(Long id, Long id1);

    @Query("SELECT gu.id FROM GameUsers gu WHERE gu.user.id = :userId")
    Long findIdByUserId(@Param("userId") Long userId);

    @Query("SELECT gu.id FROM GameUsers gu WHERE gu.user.id = :userId AND gu.game.id = :gameId")
    Long findIdByUserIdAndGameId(@Param("userId") Long userId, @Param("gameId") Long gameId);

    @Query("SELECT COUNT(gu) FROM GameUsers gu WHERE gu.game.id = :gameId AND gu.role = :role")
    long countByGameIdAndRole(@Param("gameId") Long gameId, @Param("role") GameUsersRole role);

    @Query("SELECT gu FROM GameUsers gu WHERE gu.game.id = :gameId AND gu.user.id = :userId")
    Optional<GameUsers> findUserInGame(@Param("gameId") Long gameId, @Param("userId") Long userId);

    boolean existsByGameIdAndUserId(Long gameId, Long id);

    GameUsers findByGameIdAndUserId(Long gameId, Long id);
}