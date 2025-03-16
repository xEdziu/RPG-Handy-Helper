package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CpRedCharactersService {

    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Autowired
    public CpRedCharactersService(CpRedCharactersRepository cpRedCharactersRepository,
                                  UserRepository userRepository,
                                  GameRepository gameRepository) {
        this.cpRedCharactersRepository = cpRedCharactersRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }


    public List<CpRedCharactersDTO> getAllCharacters() {
        List<CpRedCharacters> characters = cpRedCharactersRepository.findAll();

        return characters.stream().map(character ->
                new CpRedCharactersDTO(
                        character.getId(),
                        character.getGame().getName(),
                        character.getUser() != null ? character.getUser().getUsername() : null,
                        character.getName(),
                        character.getNickname(),
                        character.getType().name(),
                        character.getExpAll(),
                        character.getExpAvailable(),
                        character.getCasch()
                )
        ).toList();
    }

    public CpRedCharactersDTO getCharacter(Long characterId) {
        Optional<CpRedCharacters> character = cpRedCharactersRepository.findById(characterId);
        if (character.isPresent()) {
            CpRedCharacters cpRedCharacter = character.get();
            return new CpRedCharactersDTO(
                    cpRedCharacter.getId(),
                    cpRedCharacter.getGame().getName(),
                    cpRedCharacter.getUser() != null ? cpRedCharacter.getUser().getUsername() : null,
                    cpRedCharacter.getName(),
                    cpRedCharacter.getNickname(),
                    cpRedCharacter.getType().name(),
                    cpRedCharacter.getExpAll(),
                    cpRedCharacter.getExpAvailable(),
                    cpRedCharacter.getCasch()
            );
        } else {
            throw new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje");
        }
    }

    public Map<String, Object> createCharacter(CpRedCharacters character) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findGameById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + character.getGame().getId() + " nie została znaleziona."));

        CpRedCharacters cpRedCharacter = new CpRedCharacters();
        cpRedCharacter.setGame(game);
        cpRedCharacter.setUser(currentUser);
        cpRedCharacter.setName(character.getName());
        cpRedCharacter.setNickname(character.getNickname());
        cpRedCharacter.setType(character.getType());
        cpRedCharacter.setExpAll(character.getExpAll());
        cpRedCharacter.setExpAvailable(character.getExpAvailable());
        cpRedCharacter.setCasch(character.getCasch());

        cpRedCharactersRepository.save(cpRedCharacter);

        return Map.of("message", "Postać " + character.getName() + " została utworzona");
    }

    public Map<String, Object> createCharacterNpc(CpRedCharacters character) {
        Game game = gameRepository.findGameById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + character.getGame().getId() + " nie została znaleziona."));

        CpRedCharacters cpRedCharacter = new CpRedCharacters();
        cpRedCharacter.setGame(game);
        cpRedCharacter.setName(character.getName());
        cpRedCharacter.setNickname(character.getNickname());
        cpRedCharacter.setType(CpRedCharactersType.NPC);
        cpRedCharacter.setExpAll(character.getExpAll());
        cpRedCharacter.setExpAvailable(character.getExpAvailable());
        cpRedCharacter.setCasch(character.getCasch());

        cpRedCharactersRepository.save(cpRedCharacter);

        return Map.of("message", "Postać NPC " + character.getName() + " została utworzona");
    }


}
