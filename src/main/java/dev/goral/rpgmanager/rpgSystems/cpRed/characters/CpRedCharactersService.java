package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRole;
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
    private final GameUsersRepository gameUsersRepository;

    public Map<String, Object> getAllCharacters() {
        List<CpRedCharacters> characters = cpRedCharactersRepository.findAll();

        List<CpRedCharactersDTO> charactersDTOS = characters.stream().map(character ->
                new CpRedCharactersDTO(
                        character.getId(),
                        character.getGame().getId(),
                        character.getUser() != null ? character.getUser().getId() : null,
                        character.getName(),
                        character.getNickname(),
                        character.getType().name(),
                        character.getExpAll(),
                        character.getExpAvailable(),
                        character.getCash(),
                        character.isAlive()
                )
        ).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano listę postaci.");
        response.put("characters", charactersDTOS);
        return response;
    }

    public Map<String, Object> getCharacter(Long characterId) {
        Optional<CpRedCharacters> character = cpRedCharactersRepository.findById(characterId);
        CpRedCharactersDTO charactersDTO;
        if (character.isPresent()) {
            CpRedCharacters cpRedCharacter = character.get();
            charactersDTO = new CpRedCharactersDTO(
                    cpRedCharacter.getId(),
                    cpRedCharacter.getGame().getId(),
                    cpRedCharacter.getUser() != null ? cpRedCharacter.getUser().getId() : null,
                    cpRedCharacter.getName(),
                    cpRedCharacter.getNickname(),
                    cpRedCharacter.getType().name(),
                    cpRedCharacter.getExpAll(),
                    cpRedCharacter.getExpAvailable(),
                    cpRedCharacter.getCash(),
                    cpRedCharacter.isAlive()
            );
        } else {
            throw new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje");
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano postać o id " + characterId);
        response.put("character", charactersDTO);
        return response;
    }

    public Map<String, Object> createCharacter(CpRedCharacters character) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

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

        // Check if the user belongs to the game
        boolean userBelongsToGame = gameUsersRepository.existsByUserIdAndGameId(currentUser.getId(), game.getId());
        if (!userBelongsToGame) {
            throw new IllegalArgumentException("Użytkownik nie należy do tej gry.");
        }

        // Check if the user is a player and ensure they can only create one character
        GameUsers gameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie jest przypisany do tej gry."));

        // Sprawdź, czy to GM chce utworzyć NPC
        if (character.getType() == CpRedCharactersType.NPC && gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GameMaster może tworzyć postacie NPC.");
        }

        if (gameUser.getRole() == GameUsersRole.PLAYER) {
            Long characterCount = cpRedCharactersRepository.countByUserIdAndGameId(currentUser.getId(), game.getId());
            if (characterCount >= 1) {
                throw new IllegalStateException("Gracz może stworzyć tylko jedną postać w tej grze.");
            }
        } else if (gameUser.getRole() == GameUsersRole.SPECTATOR) {
            throw new IllegalStateException("Spectator nie może tworzyć postaci w tej grze.");
        }

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacters cpRedCharacterToUpdate = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje"));


        Game game = cpRedCharacterToUpdate.getGame();

        // Sprawdzenie, czy użytkownik należy do gry
        GameUsers gameUser = gameUsersRepository.findByGameIdAndUserId(game.getId(), currentUser.getId());

        if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER){
            if (cpRedCharacterToUpdate.getUser() != null && !cpRedCharacterToUpdate.getUser().getId().equals(currentUser.getId())){
                throw new IllegalArgumentException("Nie masz uprawnień do modyfikacji tej postaci.");
            }
            if (cpRedCharacterToUpdate.getUser() == null) {
                throw new IllegalArgumentException("Tylko GameMaster może modyfikować tę postać.");
            }

        }
//        if (cpRedCharacterToUpdate.getUser() != null && !cpRedCharacterToUpdate.getUser().getId().equals(currentUser.getId())) {
//            if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER) {
//                throw new IllegalArgumentException("Nie masz uprawnień do modyfikacji tej postaci.");
//            }
//        }
//
//        if (cpRedCharacterToUpdate.getUser() == null) {
//            if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER) {
//                throw new IllegalArgumentException("Tylko GameMaster może modyfikować tę postać.");
//            }
//        }


        if (character.getUser() != null) {
            boolean userBelongsToGame = gameUsersRepository.existsByUserIdAndGameId(character.getUser().getId(), game.getId());
            if (!userBelongsToGame) {
                throw new IllegalArgumentException("Użytkownik o id " + character.getUser().getId() + " nie należy do tej gry.");
            }
        }

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

        String cpRedCharacterToUpdateName = cpRedCharacterToUpdate.getName();

        if(character.getName() != null) {
            String characterName = character.getName().trim();
            if(characterName.isEmpty()) {
                throw new IllegalStateException("Nazwa postaci nie może być pusta");
            }
            if (characterName.length() > 255) {
                throw new IllegalStateException("Nazwa postaci nie może mieć więcej niż 255 znaków.");
            }

            if(cpRedCharactersRepository.existsByGameIdAndName(game.getId(), character.getName())) {
                throw new IllegalStateException("Postać o nazwie " + character.getName() + " już istnieje");
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
            gameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie jest przypisany do tej gry."));
            if (gameUser.getRole() != GameUsersRole.GAMEMASTER) {
                throw new IllegalArgumentException("Tylko GameMaster może zmienić właściciela postaci.");
            }

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacters cpRedCharacter = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje"));

        Game game = cpRedCharacter.getGame();

        GameUsers gameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie jest przypisany do tej gry."));
        if (gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Tylko GameMaster może zmienić postać na NPC.");
        }

        String cpRedCharacterToUpdateName = cpRedCharacter.getName();

        if (cpRedCharacter.getType() == CpRedCharactersType.NPC) {
            throw new IllegalStateException("Postać o id " + characterId + " jest już NPC");
        }

        cpRedCharacter.setUser(null);
        cpRedCharacter.setType(CpRedCharactersType.NPC);

        cpRedCharactersRepository.save(cpRedCharacter);

        return CustomReturnables.getOkResponseMap("Postać " + cpRedCharacterToUpdateName + " została zaktualizowana");
    }

    public Map<String, Object> changeAlive(Long characterId) {
        if (characterId == null) {
            throw new IllegalStateException("Należy podać id postaci");
        }

        CpRedCharacters cpRedCharacter = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + characterId + " nie istnieje"));

        // Sprawdzenie, czy GM dokonuje zmiany
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = cpRedCharacter.getGame();

        GameUsers gameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie jest przypisany do tej gry."));

        if (gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Tylko GameMaster może zmienić, czy postać żyje.");
        }

        String cpRedCharacterToUpdateName = cpRedCharacter.getName();

        if (cpRedCharacter.isAlive()) {
            cpRedCharacter.setAlive(false);
        } else {
            cpRedCharacter.setAlive(true);
        }

        cpRedCharactersRepository.save(cpRedCharacter);

        return CustomReturnables.getOkResponseMap("Zmieniono status postaci " + cpRedCharacter.getName() + " na " + cpRedCharacter.isAlive());
    }
}