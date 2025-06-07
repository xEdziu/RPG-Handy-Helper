package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo;



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
public class CpRedCharacterOtherInfoService {
    private final CpRedCharacterOtherInfoRepository CpRedCharacterOtherInfoRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private  final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterOtherInfoRepository cpRedCharacterOtherInfoRepository;


    public Map<String, Object> getAllOtherInfo() {
        List<CpRedCharacterOtherInfo> OtherInfo = CpRedCharacterOtherInfoRepository.findAll();
        List<CpRedCharacterOtherInfoDTO> OtherInfoDTO = OtherInfo.stream().map(info ->
                new CpRedCharacterOtherInfoDTO(
                        info.getCharacterId().getId(),
                        info.getNotes(),
                        info.getAddictions(),
                        info.getReputation(),
                        info.getStyle(),
                        info.getClassLifePath(),
                        info.getAccommodation(),
                        info.getRental(),
                        info.getLivingStandard()
                )).toList();
        if (OtherInfoDTO.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak dodatkowych informacji dla tej postaci.");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie dodatkowe informację dla tej postaci.");
        response.put("OtherInfo", OtherInfoDTO);
        return response;
    }

    public  Map<String, Object> getOtherInfoById(Long infoId) {
        CpRedCharacterOtherInfoDTO info = CpRedCharacterOtherInfoRepository.findById(infoId)
                .map(cpRedCharacterOtherInfo -> new CpRedCharacterOtherInfoDTO(
                        cpRedCharacterOtherInfo.getCharacterId().getId(),
                        cpRedCharacterOtherInfo.getNotes(),
                        cpRedCharacterOtherInfo.getAddictions(),
                        cpRedCharacterOtherInfo.getReputation(),
                        cpRedCharacterOtherInfo.getStyle(),
                        cpRedCharacterOtherInfo.getClassLifePath(),
                        cpRedCharacterOtherInfo.getAccommodation(),
                        cpRedCharacterOtherInfo.getRental(),
                        cpRedCharacterOtherInfo.getLivingStandard()
                )).orElseThrow(() -> new ResourceNotFoundException("Dodatkowe Informacje o id " + infoId + " nie zostały znalezione"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano dodatkowe informacje dla tej postaci.");
        response.put("otherInfo", info);
        return response;
    }

    public Map<String, Object> getOtherInfoByCharacterId(Long characterId) {
        List<CpRedCharacterOtherInfo> OtherInfo = CpRedCharacterOtherInfoRepository.findAllByCharacterId_Id(characterId);
        if (OtherInfo.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak dodatkowych informacji dla postaci o id " + characterId);
        }
        List<CpRedCharacterOtherInfoDTO> OtherInfoDTO = OtherInfo.stream().map(info ->
                new CpRedCharacterOtherInfoDTO(
                        info.getCharacterId().getId(),
                        info.getNotes(),
                        info.getAddictions(),
                        info.getReputation(),
                        info.getStyle(),
                        info.getClassLifePath(),
                        info.getAccommodation(),
                        info.getRental(),
                        info.getLivingStandard()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano dodatkowe informacje dla postaci o id " + characterId);
        response.put("OtherInfo", OtherInfoDTO);
        return response;
    }

    public Map<String, Object> getAllOtherInfoForAdmin() {
        List<CpRedCharacterOtherInfo> allOtherInfo = CpRedCharacterOtherInfoRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie dodatkowe informacje.");
        response.put("OtherInfo", allOtherInfo);
        return response;
    }

    public Map<String,Object> addOtherInfo(CpRedCharacterOtherInfoRequest cpRedCharacterOtherInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if(
                cpRedCharacterOtherInfo.getNotes() == null ||
                cpRedCharacterOtherInfo.getAddictions() == null ||
                cpRedCharacterOtherInfo.getReputation() == null ||
                cpRedCharacterOtherInfo.getStyle() == null ||
                cpRedCharacterOtherInfo.getClassLifePath() == null ||
                cpRedCharacterOtherInfo.getAccommodation() == null ||
                cpRedCharacterOtherInfo.getRental() == -1 ||
                cpRedCharacterOtherInfo.getLivingStandard() == null
        ) {
            throw new IllegalArgumentException("Wszystkie pola są wymagane.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(cpRedCharacterOtherInfo.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + cpRedCharacterOtherInfo.getCharacterId() + " nie została znaleziona"));

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

        if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania dodatkowych infomacji dla tej postaci.");
            }
        }

        if( CpRedCharacterOtherInfoRepository.existsByCharacterId(character)) {
            throw new IllegalArgumentException("Dodatkowe informacje o tej nazwie już istnieją dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getNotes().isEmpty()||
                cpRedCharacterOtherInfo.getNotes().trim().isEmpty()){
            throw new IllegalArgumentException("Notatka nie może być pusta dla tej postaci..");
        }
        if(cpRedCharacterOtherInfo.getNotes().length()>500){
            throw new IllegalArgumentException("Notatka nie może być dłuższa niż 500 znaków dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getAddictions().length()>500) {
            throw new IllegalArgumentException("Informacja o uzależnieniu nie może być dłuższa niż 500 znaków dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getReputation().isEmpty()||
                cpRedCharacterOtherInfo.getReputation().trim().isEmpty()){
            throw new IllegalArgumentException("Informacja o reputacji nie może być pusta dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getReputation().length()>500) {
            throw new IllegalArgumentException("Informacja o reputacji nie może być dłuższa niż 500 znaków dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getStyle().isEmpty()||
                cpRedCharacterOtherInfo.getStyle().trim().isEmpty()){
            throw new IllegalArgumentException("Informacja o stylu nie może być pusta dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getStyle().length()>500) {
            throw new IllegalArgumentException("Informacja o stylu nie może być dłuższa niż 500 znaków dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getClassLifePath().isEmpty()||
                cpRedCharacterOtherInfo.getClassLifePath().trim().isEmpty()){
            throw new IllegalArgumentException("Informacja o ścieżce życiowej nie może być pusta dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getClassLifePath().length()>500) {
            throw new IllegalArgumentException("Informacja ścieżce życiowej nie może być dłuższa niż 500 znaków dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getAccommodation().isEmpty()||
                cpRedCharacterOtherInfo.getAccommodation().trim().isEmpty()){
            throw new IllegalArgumentException("Informacja o zakwaterowaniu nie może być pusta dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getAccommodation().length()>500) {
            throw new IllegalArgumentException("Informacja o zakwaterowaniu nie może być dłuższa niż 500 znaków dla tej postaci.");
        }

        if(cpRedCharacterOtherInfo.getRental()<=0){
            throw new IllegalArgumentException("Wysokość czynszu musi być większa od 0 dla tej postaci.");
        }

        if(cpRedCharacterOtherInfo.getLivingStandard().isEmpty()||
                cpRedCharacterOtherInfo.getLivingStandard().trim().isEmpty()){
            throw new IllegalArgumentException("Informacja o standardzie życia nie może być pusta dla tej postaci.");
        }
        if(cpRedCharacterOtherInfo.getLivingStandard().length()>500) {
            throw new IllegalArgumentException("Informacja o standardzie życia nie może być dłuższa niż 500 znaków dla tej postaci.");
        }

        CpRedCharacterOtherInfo newInfo = new CpRedCharacterOtherInfo(
                null,
                character,
                cpRedCharacterOtherInfo.getNotes(),
                cpRedCharacterOtherInfo.getAddictions(),
                cpRedCharacterOtherInfo.getReputation(),
                cpRedCharacterOtherInfo.getStyle(),
                cpRedCharacterOtherInfo.getClassLifePath(),
                cpRedCharacterOtherInfo.getAccommodation(),
                cpRedCharacterOtherInfo.getRental(),
                cpRedCharacterOtherInfo.getLivingStandard()
        );
        cpRedCharacterOtherInfoRepository.save(newInfo);
        return CustomReturnables.getOkResponseMap("Dodatkowe informacje zostały dodane.");
    }
    public Map<String, Object> deleteOtherInfo(Long infoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterOtherInfo info = cpRedCharacterOtherInfoRepository.findById(infoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dodatkowe informacje o id " + infoId + " nie zostały znalezione"));

        CpRedCharacters character = cpRedCharactersRepository.findById(info.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + info.getCharacterId().getId() + " nie została znaleziona"));

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

        if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do usuwania dodatkowych informacji dla tej postaci.");
            }
        }
        cpRedCharacterOtherInfoRepository.deleteById(infoId);
        return CustomReturnables.getOkResponseMap("Dodatkowe informacje o id " + infoId + " zostały usunięte.");
    }

    public Map<String, Object> updateOtherInfo(Long infoId, CpRedCharacterOtherInfoRequest cpRedCharacterOtherInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterOtherInfo infoToUpdate = cpRedCharacterOtherInfoRepository.findById(infoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dodatkowe informacje o id " + infoId + " nie został znaleziony"));

        CpRedCharacters character = cpRedCharactersRepository.findById(infoToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + infoToUpdate.getCharacterId().getId() + " nie została znaleziona"));

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

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do modyfikacji dodatkowych informacji dla tej postaci.");
            }
        }

        if (infoToUpdate.getCharacterId().getId() != cpRedCharacterOtherInfo.getCharacterId()) {
            throw new IllegalArgumentException("Nie można zmienić postaci przypisanej do tych dodatkowych informacji.");
        }
        if (cpRedCharacterOtherInfo.getNotes() != null) {
            if (cpRedCharacterOtherInfo.getNotes().isEmpty() ||
                    cpRedCharacterOtherInfo.getNotes().trim().isEmpty()) {
                throw new IllegalArgumentException("Notatka nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterOtherInfo.getNotes().length() > 500) {
                throw new IllegalArgumentException("Notatka nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            infoToUpdate.setNotes(cpRedCharacterOtherInfo.getNotes());
        }
        if (cpRedCharacterOtherInfo.getAddictions() != null) {
            if (cpRedCharacterOtherInfo.getAddictions().length() > 500) {
                throw new IllegalArgumentException("Informacja o uzależnieniu nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            infoToUpdate.setAddictions(cpRedCharacterOtherInfo.getAddictions());
        }
        if( cpRedCharacterOtherInfo.getReputation() != null) {
            if (cpRedCharacterOtherInfo.getReputation().isEmpty() ||
                    cpRedCharacterOtherInfo.getReputation().trim().isEmpty()) {
                throw new IllegalArgumentException("Informacja o reputacji nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterOtherInfo.getReputation().length() > 500) {
                throw new IllegalArgumentException("Informacja o reputacji nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            infoToUpdate.setReputation(cpRedCharacterOtherInfo.getReputation());
        }
        if (cpRedCharacterOtherInfo.getStyle() != null) {
            if (cpRedCharacterOtherInfo.getStyle().isEmpty() ||
                    cpRedCharacterOtherInfo.getStyle().trim().isEmpty()) {
                throw new IllegalArgumentException("Informacja o stylu nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterOtherInfo.getStyle().length() > 500) {
                throw new IllegalArgumentException("Informacja o stylu nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            infoToUpdate.setStyle(cpRedCharacterOtherInfo.getStyle());
        }
        if (cpRedCharacterOtherInfo.getClassLifePath() != null) {
            if (cpRedCharacterOtherInfo.getClassLifePath().isEmpty() ||
                    cpRedCharacterOtherInfo.getClassLifePath().trim().isEmpty()) {
                throw new IllegalArgumentException("Informacja o ścieżce życiowej nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterOtherInfo.getClassLifePath().length() > 500) {
                throw new IllegalArgumentException("Informacja o ścieżce życiowej nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            infoToUpdate.setClassLifePath(cpRedCharacterOtherInfo.getClassLifePath());
        }
        if (cpRedCharacterOtherInfo.getAccommodation() != null) {
            if (cpRedCharacterOtherInfo.getAccommodation().isEmpty() ||
                    cpRedCharacterOtherInfo.getAccommodation().trim().isEmpty()) {
                throw new IllegalArgumentException("Informacja o zakwaterowaniu nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterOtherInfo.getAccommodation().length() > 500) {
                throw new IllegalArgumentException("Informacja o zakwaterowaniu nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            infoToUpdate.setAccommodation(cpRedCharacterOtherInfo.getAccommodation());
        }
        if (cpRedCharacterOtherInfo.getRental() != -1) {
            if (cpRedCharacterOtherInfo.getRental() <= 0) {
                throw new IllegalArgumentException("Wysokość czynszu musi być większa od 0 dla tej postaci.");
            }
            infoToUpdate.setRental(cpRedCharacterOtherInfo.getRental());
        }
        if (cpRedCharacterOtherInfo.getLivingStandard() != null) {
            if (cpRedCharacterOtherInfo.getLivingStandard().isEmpty() ||
                    cpRedCharacterOtherInfo.getLivingStandard().trim().isEmpty()) {
                throw new IllegalArgumentException("Informacja o standardzie życia nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterOtherInfo.getLivingStandard().length() > 500) {
                throw new IllegalArgumentException("Informacja o standardzie życia nie może być dłuższa niż 500 znaków dla tej postaci.");
            }
            infoToUpdate.setLivingStandard(cpRedCharacterOtherInfo.getLivingStandard());
        }
        cpRedCharacterOtherInfoRepository.save(infoToUpdate);
        return CustomReturnables.getOkResponseMap("Dodatkowe informacje zostały zaktualizowane.");
    }
}
