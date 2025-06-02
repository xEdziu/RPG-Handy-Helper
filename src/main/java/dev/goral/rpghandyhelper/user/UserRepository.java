package dev.goral.rpghandyhelper.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
    @Query("SELECT u FROM User u WHERE u.oAuthId = ?1")
    Optional<User> findByOAuthId(String oAuthId);

}
