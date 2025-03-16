package dev.goral.rpgmanager.game.gameUsers;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameUsersRepository extends JpaRepository<GameUsers, Long> {
    Optional<GameUsers> findByUserUsername(String username);
    Optional<GameUsers> findGameUsersByGame(Game game);
    List<GameUsers> findGameAllUsersByGameId(Long gameId);

}
