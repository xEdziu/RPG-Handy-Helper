package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths;


import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths.*;
import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo.CpRedCharacterOtherInfo;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo.CpRedCharacterOtherInfoRequest;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCharacterLifePathsService {
    private final dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths.CpRedCharacterLifePathsRepository CpRedCharacterLifePathsRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterLifePathsRepository cpRedCharacterLifePathsRepository;


    public Map<String, Object> getAllLifePaths() {
        List<CpRedCharacterLifePaths> LifePaths = CpRedCharacterLifePathsRepository.findAll();
        List<CpRedCharacterLifePathsDTO> LifePathsDTO = LifePaths.stream().map(path ->
                new CpRedCharacterLifePathsDTO(
                        path.getCharacterId().getId(),
                        path.getCultureOfOrigin(),
                        path.getYourCharacter(),
                        path.getClothingAndStyle(),
                        path.getHair(),
                        path.getMostValue(),
                        path.getRelationships(),
                        path.getMostImportantPerson(),
                        path.getMostImportantItem(),
                        path.getFamilyBackground(),
                        path.getFamilyEnvironment(),
                        path.getFamilyCrisis(),
                        path.getLifeGoals()
                )).toList();
        if (LifePathsDTO.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak ścieżki życiowej dla tej postaci.");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie ścieżki życiowe dla tej postaci.");
        response.put("LifePaths", LifePathsDTO);
        return response;
    }

    public Map<String, Object> getLifePathById(Long pathId) {
        CpRedCharacterLifePathsDTO path = CpRedCharacterLifePathsRepository.findById(pathId)
                .map(cpRedCharacterLifePaths -> new CpRedCharacterLifePathsDTO(
                        cpRedCharacterLifePaths.getCharacterId().getId(),
                        cpRedCharacterLifePaths.getCultureOfOrigin(),
                        cpRedCharacterLifePaths.getYourCharacter(),
                        cpRedCharacterLifePaths.getClothingAndStyle(),
                        cpRedCharacterLifePaths.getHair(),
                        cpRedCharacterLifePaths.getMostValue(),
                        cpRedCharacterLifePaths.getRelationships(),
                        cpRedCharacterLifePaths.getMostImportantPerson(),
                        cpRedCharacterLifePaths.getMostImportantItem(),
                        cpRedCharacterLifePaths.getFamilyBackground(),
                        cpRedCharacterLifePaths.getFamilyEnvironment(),
                        cpRedCharacterLifePaths.getFamilyCrisis(),
                        cpRedCharacterLifePaths.getLifeGoals()
                )).orElseThrow(() -> new ResourceNotFoundException("Ścieżka życiowa o id " + pathId + " nie została znaleziona"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano ścieżkę życiową dla tej postaci.");
        response.put("LifePath", path);
        return response;
    }

    public Map<String, Object> getLifePathByCharacterId(Long characterId) {
        List<CpRedCharacterLifePaths> LifePaths = CpRedCharacterLifePathsRepository.findAllByCharacterId_Id(characterId);
        if (LifePaths.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak ścieżki życiowej dla postaci o id " + characterId);
        }
        List<CpRedCharacterLifePathsDTO> LifePathsDTO = LifePaths.stream().map(path ->
                new CpRedCharacterLifePathsDTO(
                        path.getCharacterId().getId(),
                        path.getCultureOfOrigin(),
                        path.getYourCharacter(),
                        path.getClothingAndStyle(),
                        path.getHair(),
                        path.getMostValue(),
                        path.getRelationships(),
                        path.getMostImportantPerson(),
                        path.getMostImportantItem(),
                        path.getFamilyBackground(),
                        path.getFamilyEnvironment(),
                        path.getFamilyCrisis(),
                        path.getLifeGoals()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano ścieżkę życiową dla postaci o id " + characterId);
        response.put("LifePath", LifePathsDTO);
        return response;
    }

    public Map<String, Object> getAllLifePathsForAdmin() {
        List<CpRedCharacterLifePaths> allLifePaths = CpRedCharacterLifePathsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie ścieżki życiowe.");
        response.put("LifePaths", allLifePaths);
        return response;
    }

    public Map<String, Object> addLifePath(CpRedCharacterLifePathsRequest cpRedCharacterLifePaths) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (
                cpRedCharacterLifePaths.getYourCharacter() == null ||
                        cpRedCharacterLifePaths.getClothingAndStyle() == null ||
                        cpRedCharacterLifePaths.getHair() == null ||
                        cpRedCharacterLifePaths.getMostValue() == null ||
                        cpRedCharacterLifePaths.getRelationships() == null ||
                        cpRedCharacterLifePaths.getMostImportantPerson() == null ||
                        cpRedCharacterLifePaths.getMostImportantItem() == null ||
                        cpRedCharacterLifePaths.getFamilyBackground() == null ||
                        cpRedCharacterLifePaths.getFamilyEnvironment() == null ||
                        cpRedCharacterLifePaths.getFamilyCrisis() == null ||
                        cpRedCharacterLifePaths.getLifeGoals() == null
        ) {
            throw new IllegalArgumentException("Wszystkie pola są wymagane.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(cpRedCharacterLifePaths.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + cpRedCharacterLifePaths.getCharacterId() + " nie została znaleziona"));

        Game game = character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }

        GameUsers gameUsers = gameUsersRepository.findByGameIdAndUserId(character.getGame().getId(), currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if (character.getUser() == null) {
            if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania ścieżki życiowej w dla tej postaci.");
            }
        } else if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania ścieżki życiowej dla tej postaci.");
            }
        }

        if (CpRedCharacterLifePathsRepository.existsByCharacterId(character)) {
            throw new IllegalArgumentException("Ścieżka życiowa już istnieje dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getCultureOfOrigin().isEmpty() ||
                cpRedCharacterLifePaths.getCultureOfOrigin().trim().isEmpty()) {
            throw new IllegalArgumentException("Kultura pochodzenia nie może być puste dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getCultureOfOrigin().length() > 500) {
            throw new IllegalArgumentException("Kultura pochodzenia nie może być dłuższa niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getYourCharacter().isEmpty() ||
                cpRedCharacterLifePaths.getYourCharacter().trim().isEmpty()) {
            throw new IllegalArgumentException("Twój charakter nie może być pusty dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getYourCharacter().length() > 500) {
            throw new IllegalArgumentException("Twój charakter nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getClothingAndStyle().isEmpty() ||
                cpRedCharacterLifePaths.getClothingAndStyle().trim().isEmpty()) {
            throw new IllegalArgumentException("Styl ubioru nie może być pusty dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getClothingAndStyle().length() > 500) {
            throw new IllegalArgumentException("Styl ubioru nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getHair().isEmpty() ||
                cpRedCharacterLifePaths.getHair().trim().isEmpty()) {
            throw new IllegalArgumentException("Rodzaj fryzury nie może być pusty dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getHair().length() > 500) {
            throw new IllegalArgumentException("Rodzaj fryzury nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getMostValue().isEmpty() ||
                cpRedCharacterLifePaths.getMostValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Wartość życiowa nie może być pusta dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getMostValue().length() > 500) {
            throw new IllegalArgumentException("Wartość życiowa nie może być dłuższa niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getRelationships().isEmpty() ||
                cpRedCharacterLifePaths.getRelationships().trim().isEmpty()) {
            throw new IllegalArgumentException("Relacje nie mogą być puste dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getRelationships().length() > 500) {
            throw new IllegalArgumentException("Relacje nie mogą być dłuższe niż 500 znaków dla tej postaci.");
        }

        if (cpRedCharacterLifePaths.getMostImportantPerson().isEmpty() ||
                cpRedCharacterLifePaths.getMostImportantPerson().trim().isEmpty()) {
            throw new IllegalArgumentException("Najważniejsza osoba nie może być pusta dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getMostImportantPerson().length() > 255) {
            throw new IllegalArgumentException("Najważniejsza osoba nie może być dłuższa niż 255 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getMostImportantItem().isEmpty() ||
                cpRedCharacterLifePaths.getMostImportantItem().trim().isEmpty()) {
            throw new IllegalArgumentException("Najważniejszy przedmiot nie może być pusty dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getMostImportantItem().length() > 255) {
            throw new IllegalArgumentException("Najważniejszy przedmiot nie może być dłuższy niż 255 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getFamilyBackground().isEmpty() ||
                cpRedCharacterLifePaths.getFamilyBackground().trim().isEmpty()) {
            throw new IllegalArgumentException("Tło rodzinne nie może być puste dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getFamilyBackground().length() > 500) {
            throw new IllegalArgumentException("Środowisko rodzinne nie może być dłuższe niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getFamilyEnvironment().isEmpty() ||
                cpRedCharacterLifePaths.getFamilyEnvironment().trim().isEmpty()) {
            throw new IllegalArgumentException("Środowisko rodzinne nie może być puste dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getFamilyEnvironment().length() > 500) {
            throw new IllegalArgumentException("Środowisko rodzinne nie może być dłuższe niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getFamilyCrisis().isEmpty() ||
                cpRedCharacterLifePaths.getFamilyCrisis().trim().isEmpty()) {
            throw new IllegalArgumentException("Kryzys rodzinny nie może być pusty dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getFamilyCrisis().length() > 500) {
            throw new IllegalArgumentException("Kryzys rodzinne nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getLifeGoals().isEmpty() ||
                cpRedCharacterLifePaths.getLifeGoals().trim().isEmpty()) {
            throw new IllegalArgumentException("Cele życiowe nie mogą być puste dla tej postaci.");
        }
        if (cpRedCharacterLifePaths.getLifeGoals().length() > 500) {
            throw new IllegalArgumentException("Cele życiowe nie mogą być dłuższe niż 500 znaków dla tej postaci.");
        }

        CpRedCharacterLifePaths newpath = new CpRedCharacterLifePaths(
                null,
                character,
                cpRedCharacterLifePaths.getCultureOfOrigin(),
                cpRedCharacterLifePaths.getYourCharacter(),
                cpRedCharacterLifePaths.getClothingAndStyle(),
                cpRedCharacterLifePaths.getHair(),
                cpRedCharacterLifePaths.getMostValue(),
                cpRedCharacterLifePaths.getRelationships(),
                cpRedCharacterLifePaths.getMostImportantPerson(),
                cpRedCharacterLifePaths.getMostImportantItem(),
                cpRedCharacterLifePaths.getFamilyBackground(),
                cpRedCharacterLifePaths.getFamilyEnvironment(),
                cpRedCharacterLifePaths.getFamilyCrisis(),
                cpRedCharacterLifePaths.getLifeGoals()

        );
        cpRedCharacterLifePathsRepository.save(newpath);
        return CustomReturnables.getOkResponseMap("Ścieżka życiowa została dodana.");
    }

    public Map<String, Object> deleteLifePath(Long pathId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterLifePaths path = cpRedCharacterLifePathsRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("Ścieżka życiowa o id " + pathId + " nie została znaleziona"));

        CpRedCharacters character = cpRedCharactersRepository.findById(path.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + path.getCharacterId().getId() + " nie została znaleziona"));

        Game game = character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }
        GameUsers gameUsers = gameUsersRepository.findByUserId(currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if (character.getUser() == null) {
            if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
                throw new IllegalStateException("Nie masz uprawnień do usuwania Ścieżki życiowej w dla tej postaci.");
            }
        } else if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do usuwania Ścieżki życiowej dla tej postaci.");
            }
        }
        cpRedCharacterLifePathsRepository.deleteById(pathId);
        return CustomReturnables.getOkResponseMap("Ścieżka życiowa o id " + pathId + " została usunięta.");
    }

    public Map<String, Object> updateLifePath(Long pathId, CpRedCharacterLifePathsRequest cpRedCharacterLifePaths) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterLifePaths pathToUpdate = cpRedCharacterLifePathsRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("Ścieżka życiowa o id " + pathId + " nie została znaleziona"));

        CpRedCharacters character = cpRedCharactersRepository.findById(pathToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + pathToUpdate.getCharacterId().getId() + " nie została znaleziona"));

        Game game = character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }

        GameUsers gameUsers = gameUsersRepository.findByGameIdAndUserId(character.getGame().getId(), currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if (character.getUser() == null) {
            if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
                throw new IllegalStateException("Nie masz uprawnień do aktualizowania ścieżki życiowej dla tej postaci.");
            }
        } else if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do aktualizowania ścieżki życiowej dla tej postaci.");
            }
        }

        if (pathToUpdate.getCharacterId().getId() != cpRedCharacterLifePaths.getCharacterId()) {
            throw new IllegalArgumentException("Nie można zmienić postaci przypisanej ścieżki życiowej.");
        }
        if (cpRedCharacterLifePaths.getCultureOfOrigin() != null) {
            if (cpRedCharacterLifePaths.getCultureOfOrigin().isEmpty() ||
                    cpRedCharacterLifePaths.getCultureOfOrigin().trim().isEmpty()) {
                throw new IllegalArgumentException("Kultura pochodzenia nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getCultureOfOrigin().length() > 500) {
                throw new IllegalArgumentException("Kultura pochodzenia nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            pathToUpdate.setCultureOfOrigin(cpRedCharacterLifePaths.getCultureOfOrigin());
        }
        if (cpRedCharacterLifePaths.getYourCharacter() != null) {
            if (cpRedCharacterLifePaths.getYourCharacter().isEmpty() ||
                    cpRedCharacterLifePaths.getYourCharacter().trim().isEmpty()) {
                throw new IllegalArgumentException("Twój charakter nie może być pusty dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getYourCharacter().length() > 500) {
                throw new IllegalArgumentException("Twój charakter nie może być dłuższy niż 500 znaków dla tej postaci.");
            }
            pathToUpdate.setYourCharacter(cpRedCharacterLifePaths.getYourCharacter());
        }
        if (cpRedCharacterLifePaths.getClothingAndStyle() != null) {
            if (cpRedCharacterLifePaths.getClothingAndStyle().isEmpty() ||
                    cpRedCharacterLifePaths.getClothingAndStyle().trim().isEmpty()) {
                throw new IllegalArgumentException("Styl ubioru nie może być pusty dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getClothingAndStyle().length() > 500) {
                throw new IllegalArgumentException("Styl ubioru nie może być dłuższy niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setClothingAndStyle(cpRedCharacterLifePaths.getClothingAndStyle());

        if (cpRedCharacterLifePaths.getHair() != null) {
            if (cpRedCharacterLifePaths.getHair().isEmpty() ||
                    cpRedCharacterLifePaths.getHair().trim().isEmpty()) {
                throw new IllegalArgumentException("Rodzaj fryzury nie może być pusty dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getHair().length() > 500) {
                throw new IllegalArgumentException("Rodzaj fryzury nie może być dłuższy niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setHair(cpRedCharacterLifePaths.getHair());
        if (cpRedCharacterLifePaths.getMostValue() != null) {
            if (cpRedCharacterLifePaths.getMostValue().isEmpty() ||
                    cpRedCharacterLifePaths.getMostValue().trim().isEmpty()) {
                throw new IllegalArgumentException("Wartość życiowa nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getMostValue().length() > 500) {
                throw new IllegalArgumentException("Wartość życiowa nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setMostValue(cpRedCharacterLifePaths.getMostValue());

        if (cpRedCharacterLifePaths.getRelationships() != null) {
            if (cpRedCharacterLifePaths.getRelationships().isEmpty() ||
                    cpRedCharacterLifePaths.getRelationships().trim().isEmpty()) {
                throw new IllegalArgumentException("Relacje nie mogą być puste dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getRelationships().length() > 500) {
                throw new IllegalArgumentException("Relacje nie mogą być dłuższe niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setRelationships(cpRedCharacterLifePaths.getRelationships());

        if (cpRedCharacterLifePaths.getMostImportantPerson() != null) {
            if (cpRedCharacterLifePaths.getMostImportantPerson().isEmpty() ||
                    cpRedCharacterLifePaths.getMostImportantPerson().trim().isEmpty()) {
                throw new IllegalArgumentException("Najważniejsza osoba nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getMostImportantPerson().length() > 255) {
                throw new IllegalArgumentException("Najważniejsza osoba nie może być dłuższa niż 255 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setMostImportantPerson(cpRedCharacterLifePaths.getMostImportantPerson());

        if (cpRedCharacterLifePaths.getMostImportantItem() != null) {
            if (cpRedCharacterLifePaths.getMostImportantItem().isEmpty() ||
                    cpRedCharacterLifePaths.getMostImportantItem().trim().isEmpty()) {
                throw new IllegalArgumentException("Najważniejszy przedmiot nie może być pusty dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getMostImportantItem().length() > 255) {
                throw new IllegalArgumentException("Najważniejszy przedmiot nie może być dłuższy niż 255 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setMostImportantItem(cpRedCharacterLifePaths.getMostImportantItem());

        if (cpRedCharacterLifePaths.getFamilyBackground() != null) {
            if (cpRedCharacterLifePaths.getFamilyBackground().isEmpty() ||
                    cpRedCharacterLifePaths.getFamilyBackground().trim().isEmpty()) {
                throw new IllegalArgumentException("Tło rodzinne nie może być puste dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getFamilyBackground().length() > 500) {
                throw new IllegalArgumentException("Tło rodzinne nie może być dłuższe niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setFamilyBackground(cpRedCharacterLifePaths.getFamilyBackground());

        if (cpRedCharacterLifePaths.getFamilyEnvironment() != null) {
            if (cpRedCharacterLifePaths.getFamilyEnvironment().isEmpty() ||
                    cpRedCharacterLifePaths.getFamilyEnvironment().trim().isEmpty()) {
                throw new IllegalArgumentException("Środowisko rodzinne nie może być puste dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getFamilyEnvironment().length() > 500) {
                throw new IllegalArgumentException("Środowisko rodzinne nie może być dłuższe niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setFamilyEnvironment(cpRedCharacterLifePaths.getFamilyEnvironment());

        if (cpRedCharacterLifePaths.getFamilyCrisis() != null) {
            if (cpRedCharacterLifePaths.getFamilyCrisis().isEmpty() ||
                    cpRedCharacterLifePaths.getFamilyCrisis().trim().isEmpty()) {
                throw new IllegalArgumentException("Kryzys rodzinny nie może być pusty dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getFamilyCrisis().length() > 500) {
                throw new IllegalArgumentException("Kryzys rodzinne nie może być dłuższy niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setFamilyCrisis(cpRedCharacterLifePaths.getFamilyCrisis());

        if (cpRedCharacterLifePaths.getLifeGoals() != null) {
            if (cpRedCharacterLifePaths.getLifeGoals().isEmpty() ||
                    cpRedCharacterLifePaths.getLifeGoals().trim().isEmpty()) {
                throw new IllegalArgumentException("Cele życiowe nie mogą być puste dla tej postaci.");
            }
            if (cpRedCharacterLifePaths.getLifeGoals().length() > 500) {
                throw new IllegalArgumentException("Cele życiowe nie mogą być dłuższe niż 500 znaków dla tej postaci.");
            }
        }
        pathToUpdate.setLifeGoals(cpRedCharacterLifePaths.getLifeGoals());

        cpRedCharacterLifePathsRepository.save(pathToUpdate);
        return CustomReturnables.getOkResponseMap("Ścieżka życiowa została zaktualizowana.");
    }
}



