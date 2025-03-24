package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CpRedCharactersService {

    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public List<CpRedCharactersDTO> getAllCharacters() {
        List<CpRedCharacters> characters = cpRedCharactersRepository.findAll();

        return characters.stream().map(character ->
                new CpRedCharactersDTO(
                        character.getId(),
                        character.getGame().getId(),
                        character.getUser() != null ? character.getUser().getId() : null,
                        character.getName(),
                        character.getNickname(),
                        character.getType().name(),
                        character.getExpAll(),
                        character.getExpAvailable(),
                        character.getCash()
                )
        ).toList();
    }

    public CpRedCharactersDTO getCharacter(Long characterId) {
        Optional<CpRedCharacters> character = cpRedCharactersRepository.findById(characterId);
        if (character.isPresent()) {
            CpRedCharacters cpRedCharacter = character.get();
            return new CpRedCharactersDTO(
                    cpRedCharacter.getId(),
                    cpRedCharacter.getGame().getId(),
                    cpRedCharacter.getUser() != null ? cpRedCharacter.getUser().getId() : null,
                    cpRedCharacter.getName(),
                    cpRedCharacter.getNickname(),
                    cpRedCharacter.getType().name(),
                    cpRedCharacter.getExpAll(),
                    cpRedCharacter.getExpAvailable(),
                    cpRedCharacter.getCash()
            );
        } else {
            throw new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje");
        }
    }

    public Map<String, Object> createCharacter(CpRedCharacters character) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if(character.getName() == null ||
                character.getNickname() == null ||
                character.getType() == null ||
                character.getExpAll() == null ||
                character.getExpAvailable() == null ||
                character.getCash() == null ||
                character.getGame() == null) {
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }

        Game game = gameRepository.findGameById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + character.getGame().getId() + " nie została znaleziona."));

        String characterName = character.getName().trim();
        if(characterName.isEmpty()) {
            throw new IllegalStateException("Nazwa postaci nie może być pusta");
        }
        if (characterName.length() > 255) {
            throw new IllegalStateException("Nazwa postaci nie może mieć więcej niż 255 znaków.");
        }

        String characterNickname = character.getNickname().trim();
        if(characterNickname.isEmpty()) {
            throw new IllegalStateException("Pseudonim postaci nie może być pusty");
        }
        if (characterNickname.length() > 255) {
            throw new IllegalStateException("Pseudonim postaci nie może mieć więcej niż 255 znaków.");
        }

        if(character.getExpAll() < 0) {
            throw new IllegalStateException("Doświadczenie postaci nie może być ujemne");
        }

        if(character.getExpAvailable() < 0) {
            throw new IllegalStateException("Dostępne doświadczenie postaci nie może być ujemne");
        }

        if(character.getCash() < 0) {
            throw new IllegalStateException("Budżet postaci nie może być ujemny");
        }

        if(cpRedCharactersRepository.existsByGameIdAndName(game.getId(), character.getName())) {
            throw new IllegalStateException("Postać o nazwie " + character.getName() + " już istnieje");
        }

        CpRedCharacters cpRedCharacter = new CpRedCharacters();
        cpRedCharacter.setGame(game);
        if(character.getType() == CpRedCharactersType.PLAYER) {
            User currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
            cpRedCharacter.setUser(currentUser);
        } else {
            cpRedCharacter.setUser(null);
        }
        cpRedCharacter.setName(characterName);
        cpRedCharacter.setNickname(characterNickname);
        cpRedCharacter.setType(character.getType());
        cpRedCharacter.setExpAll(character.getExpAll());
        cpRedCharacter.setExpAvailable(character.getExpAvailable());
        cpRedCharacter.setCash(character.getCash());
        if(character.getCharacterPhotoPath() == null) {
            character.setCharacterPhotoPath("static/img/profilePics/cyberpunkDefaultProfilePic.png");
        } else {
            character.setCharacterPhotoPath(character.getCharacterPhotoPath());
        }

        cpRedCharactersRepository.save(cpRedCharacter);

        return CustomReturnables.getOkResponseMap("Postać " + character.getName() + " została utworzona");
    }

    public Map<String, Object> updateCharacter(Long characterId, CpRedCharacters character) {

        if(character.getName() == null &&
            character.getNickname() == null &&
            character.getType() == null &&
            character.getExpAll() == null &&
            character.getExpAvailable() == null &&
            character.getCash() == null &&
            character.getCharacterPhotoPath() == null &&
            character.getUser() == null &&
            character.getGame() == null) {
            throw new IllegalStateException("Należy podać jeden z parametrów");
        }

        CpRedCharacters cpRedCharacterToUpdate = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje"));

        String cpRedCharacterToUpdateName = cpRedCharacterToUpdate.getName();

        if(character.getName() != null) {
            String characterName = character.getName().trim();
            if(characterName.isEmpty()) {
                throw new IllegalStateException("Nazwa postaci nie może być pusta");
            }
            if (characterName.length() > 255) {
                throw new IllegalStateException("Nazwa postaci nie może mieć więcej niż 255 znaków.");
            }

            cpRedCharacterToUpdate.setName(character.getName());
        }

        if(character.getNickname() != null) {
            String characterNickname = character.getNickname().trim();
            if(characterNickname.isEmpty()) {
                throw new IllegalStateException("Pseudonim postaci nie może być pusty");
            }
            if (characterNickname.length() > 255) {
                throw new IllegalStateException("Pseudonim postaci nie może mieć więcej niż 255 znaków.");
            }

            cpRedCharacterToUpdate.setNickname(character.getNickname());
        }

        if(character.getType() != null) {
            if(cpRedCharacterToUpdate.getType() == null) {
                throw new IllegalStateException("Typ postaci nie może być pusty");
            }
            cpRedCharacterToUpdate.setType(character.getType());
        }

        if(character.getExpAll() != null) {
            if(cpRedCharacterToUpdate.getExpAll() == null) {
                throw new IllegalStateException("Doświadczenie postaci nie może być puste");
            }
            if(character.getExpAll() < 0) {
                throw new IllegalStateException("Doświadczenie postaci nie może być ujemne");
            }
            cpRedCharacterToUpdate.setExpAll(character.getExpAll());
        }

        if(character.getExpAvailable() != null) {
            if(cpRedCharacterToUpdate.getExpAvailable() == null) {
                throw new IllegalStateException("Dostępne doświadczenie postaci nie może być puste");
            }
            if(character.getExpAvailable() < 0) {
                throw new IllegalStateException("Dostępne doświadczenie postaci nie może być ujemne");
            }
            cpRedCharacterToUpdate.setExpAvailable(character.getExpAvailable());
        }

        if(character.getCash() != null) {
            if(cpRedCharacterToUpdate.getCash() == null) {
                throw new IllegalStateException("Budżet postaci nie może być pusty");
            }
            if(character.getCash() < 0) {
                throw new IllegalStateException("Budżet postaci nie może być ujemny");
            }
            cpRedCharacterToUpdate.setCash(character.getCash());
        }

        if(character.getUser() != null) {
            User user = userRepository.findById(character.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Użytkownik o id " + character.getUser().getId() + " nie istnieje"));
            cpRedCharacterToUpdate.setUser(user);
        }

        if(character.getCharacterPhotoPath() != null) {
            cpRedCharacterToUpdate.setCharacterPhotoPath(character.getCharacterPhotoPath());
        }

        cpRedCharactersRepository.save(cpRedCharacterToUpdate);

        return CustomReturnables.getOkResponseMap("Postać " + cpRedCharacterToUpdateName + " została zaktualizowana");
    }

    public Map<String, Object> playerToNpc(Long characterId) {
        if (characterId == null) {
            throw new IllegalStateException("Należy podać id postaci");
        }
        CpRedCharacters cpRedCharacter = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje"));

        String cpRedCharacterToUpdateName = cpRedCharacter.getName();

        if (cpRedCharacter.getType() == CpRedCharactersType.NPC) {
            throw new IllegalStateException("Postać o id " + characterId + " jest już NPC");
        }

        cpRedCharacter.setUser(null);
        cpRedCharacter.setType(CpRedCharactersType.NPC);

        cpRedCharactersRepository.save(cpRedCharacter);

        return CustomReturnables.getOkResponseMap("Postać " + cpRedCharacterToUpdateName + " została zaktualizowana");
    }
}

