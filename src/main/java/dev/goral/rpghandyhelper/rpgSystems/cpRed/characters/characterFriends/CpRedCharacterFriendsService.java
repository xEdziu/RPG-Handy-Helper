package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends;

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
public class CpRedCharacterFriendsService {
    private final CpRedCharacterFriendsRepository CpRedCharacterFriendsRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private  final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterFriendsRepository cpRedCharacterFriendsRepository;

    public Map<String, Object> getAllFriends() {
        List<CpRedCharacterFriends> friends = CpRedCharacterFriendsRepository.findAll();
        List<CpRedCharacterFriendsDTO> friendsDTO = friends.stream().map(friend ->
                new CpRedCharacterFriendsDTO(
                        friend.getCharacterId().getId(),
                        friend.getName(),
                        friend.getDescription()
                )).toList();
        if (friendsDTO.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak przyjaciół dla tej postaci :-( .");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano przyjaciół dla tej postaci.");
        response.put("friends", friendsDTO);
        return response;
    }

    public  Map<String, Object> getFriendById(Long friendsId) {
        CpRedCharacterFriendsDTO friends = CpRedCharacterFriendsRepository.findById(friendsId)
                .map(cpRedCharacterFriends -> new CpRedCharacterFriendsDTO(
                        cpRedCharacterFriends.getCharacterId().getId(),
                        cpRedCharacterFriends.getName(),
                        cpRedCharacterFriends.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Przyjaciel o id " + friendsId + " nie został znaleziony :-("));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano przyjaciół dla tej postaci.");
        response.put("friends", friends);
        return response;
    }

    public Map<String, Object> getFriendsByCharacterId(Long characterId) {
        List<CpRedCharacterFriends> friends = CpRedCharacterFriendsRepository.findAllByCharacterId_Id(characterId);
        if (friends.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak przyjaciół dla postaci o id " + characterId + " :-(");
        }
        List<CpRedCharacterFriendsDTO> friendsDTO = friends.stream().map(friend ->
                new CpRedCharacterFriendsDTO(
                        friend.getCharacterId().getId(),
                        friend.getName(),
                        friend.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano przyjaciół w dla postaci o id " + characterId);
        response.put("friends", friendsDTO);
        return response;
    }

    public Map<String, Object> getAllFriendsForAdmin() {
        List<CpRedCharacterFriends> allFriends = CpRedCharacterFriendsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano przyjaciół w dla tej postaci.");
        response.put("friends", allFriends);
        return response;
    }

    public Map<String,Object> addFriend(CpRedCharacterFriendsRequest cpRedCharacterFriends) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if(cpRedCharacterFriends.getName() == null ||
                cpRedCharacterFriends.getDescription() == null
        ) {
            throw new IllegalArgumentException("Wszystkie pola są wymagane.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(cpRedCharacterFriends.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + cpRedCharacterFriends.getCharacterId() + " nie została znaleziona"));

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
                throw new IllegalStateException("Nie masz uprawnień do dodawania przyjaciół dla tej postaci.");
            }
        }
        else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania przyjaciół dla tej postaci.");
            }
        }

        if( CpRedCharacterFriendsRepository.existsByNameAndCharacterId(cpRedCharacterFriends.getName(), character)) {
            throw new IllegalArgumentException("Przyjaciel o tej nazwie już istnieje dla tej postaci.");
        }
        if(cpRedCharacterFriends.getName().isEmpty()||
                cpRedCharacterFriends.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Nazwa przyjaciela nie może być pusta dla tej postaci..");
        }
        if(cpRedCharacterFriends.getName().length()>255){
            throw new IllegalArgumentException("Nazwa przyjaciela nie może być dłuższa niż 255 znaków dla tej postaci.");
        }
        if(cpRedCharacterFriends.getDescription().length()>500) {
            throw new IllegalArgumentException("Opis przyjaciela nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        CpRedCharacterFriends newFriends = new CpRedCharacterFriends(
                null,
                character,
                cpRedCharacterFriends.getName(),
                cpRedCharacterFriends.getDescription()
        );
        cpRedCharacterFriendsRepository.save(newFriends);
        return CustomReturnables.getOkResponseMap("Przyjaciel został dodany.");
    }
    public Map<String, Object> deleteFriend(Long friendsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterFriends friends = cpRedCharacterFriendsRepository.findById(friendsId)
                .orElseThrow(() -> new ResourceNotFoundException("Przyjaciel o id " + friendsId + " nie został znaleziony :-("));

        CpRedCharacters character = cpRedCharactersRepository.findById(friends.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + friends.getCharacterId().getId() + " nie została znaleziona"));

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
                throw new IllegalStateException("Nie masz uprawnień do usuwania przyjaciół dla tej postaci.");
            }
        }
        else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do usuwania przyjaciół dla tej postaci.");
            }
        }
        cpRedCharacterFriendsRepository.deleteById(friendsId);
        return CustomReturnables.getOkResponseMap("Przyjaciel o id " + friendsId + " został usunięty :-( .");
    }

    public Map<String, Object> updateFriends(Long friendsId, CpRedCharacterFriendsRequest cpRedCharacterFriends) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterFriends friendsToUpdate = cpRedCharacterFriendsRepository.findById(friendsId)
                .orElseThrow(() -> new ResourceNotFoundException("Przyjaciel o id " + friendsId + " nie został znaleziony :-("));

        CpRedCharacters character = cpRedCharactersRepository.findById(friendsToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + friendsToUpdate.getCharacterId().getId() + " nie została znaleziona"));

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
                throw new IllegalStateException("Nie masz uprawnień do modyfikowania przyjaciół dla tej postaci.");
            }
        }
        else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do modyfikowania przyjaciół dla tej postaci.");
            }
        }

        if (friendsToUpdate.getCharacterId().getId() != cpRedCharacterFriends.getCharacterId()) {
            throw new IllegalArgumentException("Nie można zmienić postaci przyjaciela.");
        }
        if (cpRedCharacterFriends.getName() != null) {
            if (cpRedCharacterFriendsRepository.existsByNameAndCharacterId(cpRedCharacterFriends.getName(), character)) {
                throw new IllegalArgumentException("Przyjaciel o tej nazwie już istnieje dla tej postaci.");
            }
            if (cpRedCharacterFriends.getName().isEmpty() ||
                    cpRedCharacterFriends.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Nazwa przyjaciela nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterFriends.getName().length() > 255) {
                throw new IllegalArgumentException("Nazwa przyjaciela nie może być dłuższa niż 255 znaków dla tej postaci.");
            }
            friendsToUpdate.setName(cpRedCharacterFriends.getName());
        }

        if (cpRedCharacterFriends.getDescription() != null) {
            if (cpRedCharacterFriends.getDescription().length() > 500) {
                throw new IllegalArgumentException("Opis przyjaciela nie może być dłuższy niż 500 znaków dla tej postaci.");
            }
            friendsToUpdate.setDescription(cpRedCharacterFriends.getDescription());
        }
        cpRedCharacterFriendsRepository.save(friendsToUpdate);
        return CustomReturnables.getOkResponseMap("Przyjaciel został zaktualizowany.");
    }
}
