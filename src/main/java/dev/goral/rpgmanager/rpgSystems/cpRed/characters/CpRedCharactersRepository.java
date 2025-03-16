package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharactersRepository extends JpaRepository<CpRedCharacters, Long> {

    List<CpRedCharacters> findAll();

}
