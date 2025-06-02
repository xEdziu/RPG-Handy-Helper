package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
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
public class CpRedCustomCyberwaresService {
    private final CpRedCustomCyberwaresRepository cpRedCustomCyberwaresRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;
    public Map<String, Object> getAllCyberware() {
        List<CpRedCustomCyberwaresDTO> allCustomCyberwares=cpRedCustomCyberwaresRepository.findAll().stream()
                .map(CpRedCustomCyberwares -> new CpRedCustomCyberwaresDTO(
                        CpRedCustomCyberwares.getGameId().getId(),
                        CpRedCustomCyberwares.getName(),
                        CpRedCustomCyberwares.getMountPlace().toString(),
                        CpRedCustomCyberwares.getRequirements(),
                        CpRedCustomCyberwares.getHumanityLoss(),
                        CpRedCustomCyberwares.getSize(),
                        CpRedCustomCyberwares.getInstallationPlace().toString(),
                        CpRedCustomCyberwares.getPrice(),
                        CpRedCustomCyberwares.getAvailability().toString(),
                        CpRedCustomCyberwares.getDescription()
                )).toList();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe wszczepy zostały pobrane.");
        response.put("customCyberwares",allCustomCyberwares);
        return response;
    }


    public Map<String, Object> getCyberwareById(Long cyberwareId) {
        CpRedCustomCyberwaresDTO customCyberware = cpRedCustomCyberwaresRepository.findById(cyberwareId)
                .map(CpRedCustomCyberwares -> new CpRedCustomCyberwaresDTO(
                        CpRedCustomCyberwares.getGameId().getId(),
                        CpRedCustomCyberwares.getName(),
                        CpRedCustomCyberwares.getMountPlace().toString(),
                        CpRedCustomCyberwares.getRequirements(),
                        CpRedCustomCyberwares.getHumanityLoss(),
                        CpRedCustomCyberwares.getSize(),
                        CpRedCustomCyberwares.getInstallationPlace().toString(),
                        CpRedCustomCyberwares.getPrice(),
                        CpRedCustomCyberwares.getAvailability().toString(),
                        CpRedCustomCyberwares.getDescription()
                )).orElseThrow(()-> new ResourceNotFoundException("Customowy wszczep o id " + cyberwareId + " nie istnieje."));
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowy wszczep został pobrany.");
        response.put("customCyberware", customCyberware);
        return response;

    }
    public Map<String, Object> getCyberwareByGame(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomCyberwaresDTO> allCustomCyberwares=cpRedCustomCyberwaresRepository.findAllByGameId(game).stream()
                .map(CpRedCustomCyberwares -> new CpRedCustomCyberwaresDTO(
                        CpRedCustomCyberwares.getGameId().getId(),
                        CpRedCustomCyberwares.getName(),
                        CpRedCustomCyberwares.getMountPlace().toString(),
                        CpRedCustomCyberwares.getRequirements(),
                        CpRedCustomCyberwares.getHumanityLoss(),
                        CpRedCustomCyberwares.getSize(),
                        CpRedCustomCyberwares.getInstallationPlace().toString(),
                        CpRedCustomCyberwares.getPrice(),
                        CpRedCustomCyberwares.getAvailability().toString(),
                        CpRedCustomCyberwares.getDescription()
                )).toList();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe wszczepy do gry zostały pobrane.");
        response.put("customCyberwares",allCustomCyberwares);
        return response;
    }

    public Map<String, Object> addCyberware(CpRedCustomCyberwaresRequest cpRedCustomCyberwares) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (cpRedCustomCyberwares.getGameId()==null||
                cpRedCustomCyberwares.getName()==null||
                cpRedCustomCyberwares.getMountPlace()==null||
                cpRedCustomCyberwares.getRequirements()==null||
                cpRedCustomCyberwares.getHumanityLoss()==null||
                cpRedCustomCyberwares.getSize()<0||
                cpRedCustomCyberwares.getInstallationPlace()==null||
                cpRedCustomCyberwares.getPrice()<0||
                cpRedCustomCyberwares.getAvailability()==null||
                cpRedCustomCyberwares.getDescription()==null) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }
        Game game = gameRepository.findById(cpRedCustomCyberwares.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + cpRedCustomCyberwares.getGameId() + " nie istnieje."));
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cpRedCustomCyberwares.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać wszczep do gry.");
        }
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + cpRedCustomCyberwares.getGameId() + " nie jest aktywna.");
        }
        if (cpRedCustomCyberwaresRepository.existsByNameAndGameId(cpRedCustomCyberwares.getName(), game)) {
            throw new IllegalStateException("Customowy wszczep o tej nazwie już istnieje.");
        }
        if (cpRedCustomCyberwares.getName().isEmpty() ||
                cpRedCustomCyberwares.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa wszczepu nie może być pusta.");
        }
        if (cpRedCustomCyberwares.getName().length() > 255) {
            throw new IllegalStateException("Nazwa wszczepu nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomCyberwares.getRequirements().isEmpty() ||
                cpRedCustomCyberwares.getRequirements().trim().isEmpty()) {
            throw new IllegalStateException("Wymagania wszczepu nie mogą być puste.");
        }
        if (cpRedCustomCyberwares.getRequirements().length() > 500) {
            throw new IllegalStateException("Wymagania wszczepu nie mogą być dłuższe niż 500 znaków.");
        }
        if (cpRedCustomCyberwares.getHumanityLoss().isEmpty() ||
                cpRedCustomCyberwares.getHumanityLoss().trim().isEmpty()) {
            throw new IllegalStateException("Utrata ludzkosci wszczepu nie może być pusta.");
        }
        if (cpRedCustomCyberwares.getHumanityLoss().length() > 255) {
            throw new IllegalStateException("Utrata ludzkosci wszczepu nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomCyberwares.getDescription().isEmpty() ||
                cpRedCustomCyberwares.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis wszczepu nie może być pusty.");
        }
        if (cpRedCustomCyberwares.getDescription().length() > 500) {
            throw new IllegalStateException("Opis wszczepu nie może być dłuższy niż 500 znaków.");
        }
        if (cpRedCustomCyberwares.getSize() < 0) {
            throw new IllegalStateException("Rozmiar wszczepu nie może być mniejszy od 0.");
        }
        if (cpRedCustomCyberwares.getPrice() < 0) {
            throw new IllegalStateException("Cena wszczepu nie może być mniejsza od 0.");
        }
        CpRedCustomCyberwares newCpRedCustomCyberwares = new CpRedCustomCyberwares(
                null,
                game,
                cpRedCustomCyberwares.getName(),
                cpRedCustomCyberwares.getMountPlace(),
                cpRedCustomCyberwares.getRequirements(),
                cpRedCustomCyberwares.getHumanityLoss(),
                cpRedCustomCyberwares.getSize(),
                cpRedCustomCyberwares.getInstallationPlace(),
                cpRedCustomCyberwares.getPrice(),
                cpRedCustomCyberwares.getAvailability(),
                cpRedCustomCyberwares.getDescription()
        );
        cpRedCustomCyberwaresRepository.save(newCpRedCustomCyberwares);
        return CustomReturnables.getOkResponseMap("Customowy wszczep został dodany.");
    }

    public Map<String, Object> updateCyberware(Long cyberwareId, CpRedCustomCyberwaresRequest cpRedCustomCyberwares) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        CpRedCustomCyberwares cyberwareToUpdate = cpRedCustomCyberwaresRepository.findById(cyberwareId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowy wszczep o id " + cyberwareId + " nie istnieje."));

        Game game = gameRepository.findById(cyberwareToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id "+cyberwareToUpdate.getGameId().getId()+" nie istnieje."));
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + cyberwareToUpdate.getGameId().getId() + " nie jest aktywna.");
        }

        if (cyberwareToUpdate.getGameId().getId() != cpRedCustomCyberwares.getGameId()) {
            throw new IllegalStateException("Nie można zmienić gry dla wszczepu.");
        }

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cyberwareToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może modyfikować wszczep.");
        }
        if(cpRedCustomCyberwares.getName()!=null){
            if (cpRedCustomCyberwaresRepository.existsByNameAndGameId(cpRedCustomCyberwares.getName(), game)) {
                throw new IllegalStateException("Customowy wszczep o tej nazwie już istnieje.");
            }



            if (cpRedCustomCyberwares.getName().isEmpty() ||
                    cpRedCustomCyberwares.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa wszczepu nie może być pusta.");
            }
            if (cpRedCustomCyberwares.getName().length() > 255) {
                throw new IllegalStateException("Nazwa wszczepu nie może być dłuższa niż 255 znaków.");
            }
            cyberwareToUpdate.setName(cpRedCustomCyberwares.getName());
        }
        if(cpRedCustomCyberwares.getMountPlace()!=null){
            cyberwareToUpdate.setMountPlace(cpRedCustomCyberwares.getMountPlace());
        }
        if(cpRedCustomCyberwares.getRequirements()!=null){
            if (cpRedCustomCyberwares.getRequirements().isEmpty() ||
                    cpRedCustomCyberwares.getRequirements().trim().isEmpty()) {
                throw new IllegalStateException("Wymagania wszczepu nie mogą być puste.");
            }
            if (cpRedCustomCyberwares.getRequirements().length() > 500) {
                throw new IllegalStateException("Wymagania wszczepu nie mogą być dłuższe niż 500 znaków.");
            }
            cyberwareToUpdate.setRequirements(cpRedCustomCyberwares.getRequirements());
        }
        if(cpRedCustomCyberwares.getHumanityLoss()!=null){
            if (cpRedCustomCyberwares.getHumanityLoss().isEmpty() ||
                    cpRedCustomCyberwares.getHumanityLoss().trim().isEmpty()) {
                throw new IllegalStateException("Utrata ludzkosci wszczepu nie może być pusta.");
            }
            if (cpRedCustomCyberwares.getHumanityLoss().length() > 255) {
                throw new IllegalStateException("Utrata ludzkosci wszczepu nie może być dłuższa niż 255 znaków.");
            }
            cyberwareToUpdate.setHumanityLoss(cpRedCustomCyberwares.getHumanityLoss());
        }
        if(cpRedCustomCyberwares.getSize()!=cyberwareToUpdate.getSize()){
            if(cpRedCustomCyberwares.getSize()!=-1) {
                if (cpRedCustomCyberwares.getSize() < 0) {
                    throw new IllegalStateException("Rozmiar wszczepu nie może być mniejszy od 0.");
                }
                cyberwareToUpdate.setSize(cpRedCustomCyberwares.getSize());
            }
        }
        if(cpRedCustomCyberwares.getInstallationPlace()!=null){
            cyberwareToUpdate.setInstallationPlace(cpRedCustomCyberwares.getInstallationPlace());
        }
        if(cpRedCustomCyberwares.getPrice()!=cyberwareToUpdate.getPrice()){
            if(cpRedCustomCyberwares.getPrice()!=-1) {
                if (cpRedCustomCyberwares.getPrice() < 0) {
                    throw new IllegalStateException("Cena wszczepu nie może być mniejsza od 0.");
                }
                cyberwareToUpdate.setPrice(cpRedCustomCyberwares.getPrice());
            }
        }
        if(cpRedCustomCyberwares.getAvailability()!=null){
            cyberwareToUpdate.setAvailability(cpRedCustomCyberwares.getAvailability());
        }
        if(cpRedCustomCyberwares.getDescription()!=null){
            if (cpRedCustomCyberwares.getDescription().isEmpty() ||
                    cpRedCustomCyberwares.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis wszczepu nie może być pusty.");
            }
            if (cpRedCustomCyberwares.getDescription().length() > 500) {
                throw new IllegalStateException("Opis wszczepu nie może być dłuższy niż 500 znaków.");
            }
            cyberwareToUpdate.setDescription(cpRedCustomCyberwares.getDescription());
        }
        cpRedCustomCyberwaresRepository.save(cyberwareToUpdate);
        return CustomReturnables.getOkResponseMap("Customowy wszczep został zmodyfikowany.");
    }

    public Map<String, Object> getAllCyberwareForAdmin() {
        List<CpRedCustomCyberwares> allCustomCyberwaresList = cpRedCustomCyberwaresRepository.findAll();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe wszczepy zostały pobrane dla administratora.");
        response.put("customCyberwares",allCustomCyberwaresList);
        return response;
    }
}
