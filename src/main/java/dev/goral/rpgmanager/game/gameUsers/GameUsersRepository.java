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
    List<GameUsers> findGameAllUsersByGameId(Long gameId);

    boolean existsByUserIdAndGameId(Long id, Long id1);

    @Query("SELECT gu.id FROM GameUsers gu WHERE gu.user.id = :userId")
    Long findIdByUserId(@Param("userId") Long userId);
}
