package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterTragicLoveStory;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
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
public class CpRedCharacterTragicLoveStoryService {
    private final CpRedCharacterTragicLoveStoryRepository CpRedCharacterTragicLoveStoryRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private  final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterTragicLoveStoryRepository cpRedCharacterTragicLoveStoryRepository;

    public Map<String, Object> getAllTragicLoveStory() {
        List<CpRedCharacterTragicLoveStory> stories = CpRedCharacterTragicLoveStoryRepository.findAll();
        List<CpRedCharacterTragicLoveStoryDTO> storiesDTO = stories.stream().map(story ->
                new CpRedCharacterTragicLoveStoryDTO(
                        story.getCharacterId().getId(),
                        story.getName(),
                        story.getDescription()
                )).toList();
        if (storiesDTO.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak wątków tragiczno-miłosnych dla tej postaci.");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wątki tragiczno-miłosne dla tej postaci.");
        response.put("stories", storiesDTO);
        return response;
    }

    public  Map<String, Object> getTragicLoveStoryById(Long storyId) {
        CpRedCharacterTragicLoveStoryDTO story = CpRedCharacterTragicLoveStoryRepository.findById(storyId)
                .map(cpRedCharacterTragicLoveStory -> new CpRedCharacterTragicLoveStoryDTO(
                        cpRedCharacterTragicLoveStory.getCharacterId().getId(),
                        cpRedCharacterTragicLoveStory.getName(),
                        cpRedCharacterTragicLoveStory.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Wątek tragiczno-miłosny o id " + storyId + " nie został znaleziony"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wątek tragiczno-miłosnyc dla tej postaci.");
        response.put("story", story);
        return response;
    }

    public Map<String, Object> getTragicLoveStoryByCharacterId(Long characterId) {
        List<CpRedCharacterTragicLoveStory> stories = CpRedCharacterTragicLoveStoryRepository.findAllByCharacterId_Id(characterId);
        if (stories.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak wątku tragiczno-miłosnego dla postaci o id " + characterId);
        }
        List<CpRedCharacterTragicLoveStoryDTO> storiesDTO = stories.stream().map(story ->
                new CpRedCharacterTragicLoveStoryDTO(
                        story.getCharacterId().getId(),
                        story.getName(),
                        story.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wątek tragiczno-miłosny w dla postaci o id " + characterId);
        response.put("stories", storiesDTO);
        return response;
    }

    public Map<String, Object> getAllTragicLoveStoryForAdmin() {
        List<CpRedCharacterTragicLoveStory> allTragicLoveStory = CpRedCharacterTragicLoveStoryRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wątki tragiczno-miłosne w dla tej postaci.");
        response.put("stories", allTragicLoveStory);
        return response;
    }

    public Map<String,Object> addTragicLoveStory(CpRedCharacterTragicLoveStoryRequest cpRedCharacterTragicLoveStory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if(cpRedCharacterTragicLoveStory.getName() == null ||
                cpRedCharacterTragicLoveStory.getDescription() == null
        ) {
            throw new IllegalArgumentException("Wszystkie pola są wymagane.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(cpRedCharacterTragicLoveStory.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + cpRedCharacterTragicLoveStory.getCharacterId() + " nie została znaleziona"));

        Game game=character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus()!= GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }

        GameUsers gameUsers = gameUsersRepository.findByGameIdAndUserId(character.getGame().getId(), currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if(character.getUser()==null){
            if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
                throw new IllegalStateException("Nie masz uprawnień do dodawania wątków tragiczno-miłosnych w dla tej postaci.");
            }
        }
        else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania wątków tragiczno-miłosnych w dla tej postaci.");
            }
        }

        if( CpRedCharacterTragicLoveStoryRepository.existsByNameAndCharacterId(cpRedCharacterTragicLoveStory.getName(), character)) {
            throw new IllegalArgumentException("Wątek tragiczno-miłosny o tej nazwie już istnieje dla tej postaci.");
        }
        if(cpRedCharacterTragicLoveStory.getName().isEmpty()||
                cpRedCharacterTragicLoveStory.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Nazwa tego wątku tragiczno-miłosnego nie może być pusta dla tej postaci..");
        }
        if(cpRedCharacterTragicLoveStory.getName().length()>255){
            throw new IllegalArgumentException("Nazwa tego wątku tragiczno-miłosnego nie może być dłuższa niż 255 znaków dla tej postaci.");
        }
        if(cpRedCharacterTragicLoveStory.getDescription().length()>500) {
            throw new IllegalArgumentException("Opis tego wątku tragiczno-miłosnego nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        CpRedCharacterTragicLoveStory newTragicLoveStory = new CpRedCharacterTragicLoveStory(
                null,
                character,
                cpRedCharacterTragicLoveStory.getName(),
                cpRedCharacterTragicLoveStory.getDescription()
        );
        cpRedCharacterTragicLoveStoryRepository.save(newTragicLoveStory);
        return CustomReturnables.getOkResponseMap("Wątek tragiczno-miłosny został dodany.");
    }
    public Map<String, Object> deleteTragicLoveStory(Long storyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterTragicLoveStory story = cpRedCharacterTragicLoveStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Wróg o id " + storyId + " nie został znaleziony"));

        CpRedCharacters character = cpRedCharactersRepository.findById(story.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + story.getCharacterId().getId() + " nie została znaleziona"));

        Game game=character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus()!= GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }
        GameUsers gameUsers = gameUsersRepository.findByUserId(currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if(character.getUser()==null){
            if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
                throw new IllegalStateException("Nie masz uprawnień do dodawania wątków tragiczno-miłosnych w dla tej postaci.");
            }
        }
        else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania wątków tragiczno-miłosnych w dla tej postaci.");
            }
        }
        cpRedCharacterTragicLoveStoryRepository.deleteById(storyId);
        return CustomReturnables.getOkResponseMap("Wątek tragiczno-miłosny o id " + storyId + " został usunięty.");
    }

    public Map<String, Object> updateTragicLoveStory(Long storyId, CpRedCharacterTragicLoveStoryRequest cpRedCharacterTragicLoveStory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterTragicLoveStory storyToUpdate = cpRedCharacterTragicLoveStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Wątek tragiczno-miłosny o id " + storyId + " nie został znaleziony"));

        CpRedCharacters character = cpRedCharactersRepository.findById(storyToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + storyToUpdate.getCharacterId().getId() + " nie została znaleziona"));

        Game game=character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus()!= GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }

        GameUsers gameUsers = gameUsersRepository.findByGameIdAndUserId(character.getGame().getId(), currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if(character.getUser()==null){
            if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
                throw new IllegalStateException("Nie masz uprawnień do dodawania wątków tragiczno-miłosnych w dla tej postaci.");
            }
        }
        else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania wątków tragiczno-miłosnych w dla tej postaci.");
            }
        }

        if (storyToUpdate.getCharacterId().getId() != cpRedCharacterTragicLoveStory.getCharacterId()) {
            throw new IllegalArgumentException("Nie można zmienić postaci tego wątku tragiczno-miłosnego.");
        }
        if (cpRedCharacterTragicLoveStory.getName() != null) {
            if (cpRedCharacterTragicLoveStoryRepository.existsByNameAndCharacterId(cpRedCharacterTragicLoveStory.getName(), character)) {
                throw new IllegalArgumentException("Wątek tragiczno-miłosny o tej nazwie już istnieje dla tej postaci.");
            }
            if (cpRedCharacterTragicLoveStory.getName().isEmpty() ||
                    cpRedCharacterTragicLoveStory.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Nazwa tego wątku tragiczno-miłosnego nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterTragicLoveStory.getName().length() > 255) {
                throw new IllegalArgumentException("Nazwa tego wątku tragiczno-miłosnego nie może być dłuższa niż 255 znaków dla tej postaci.");
            }
            storyToUpdate.setName(cpRedCharacterTragicLoveStory.getName());
        }

        if (cpRedCharacterTragicLoveStory.getDescription() != null) {
            if (cpRedCharacterTragicLoveStory.getDescription().length() > 500) {
                throw new IllegalArgumentException("Opis tego wątku tragiczno-miłosnego nie może być dłuższy niż 500 znaków dla tej postaci.");
            }
            storyToUpdate.setDescription(cpRedCharacterTragicLoveStory.getDescription());
        }
        cpRedCharacterTragicLoveStoryRepository.save(storyToUpdate);
        return CustomReturnables.getOkResponseMap("Wątek tragiczno-miłosny został zaktualizowany.");
    }
}
