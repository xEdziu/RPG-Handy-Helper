package dev.goral.rpghandyhelper.game;

import dev.goral.rpghandyhelper.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findGameById(Long id);
    Optional<Game> findGameByName(String name);

    List<Game> findAllByOwner(User user);
}