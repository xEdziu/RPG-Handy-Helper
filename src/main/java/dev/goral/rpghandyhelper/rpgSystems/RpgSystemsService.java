package dev.goral.rpghandyhelper.rpgSystems;

import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RpgSystemsService {

    private final RpgSystemsRepository rpgSystemsRepository;

    public Map<String, Object> getRpgSystemsById(Long rpgSystemsId) {
        RpgSystems rpgSystems = rpgSystemsRepository.findById(rpgSystemsId)
                .orElseThrow(() -> new ResourceNotFoundException("System o id " + rpgSystemsId + " nie istnieje"));

        RpgSystemsDTO rpgSystemsDTO = new RpgSystemsDTO(
                rpgSystems.getId(),
                rpgSystems.getName(),
                rpgSystems.getDescription()
        );

        Map<String, Object> response = CustomReturnables.getOkResponseMap("System został pobrany.");
        response.put("rpgSystem", rpgSystemsDTO);
        return response;
    }

    public Map<String, Object> getAllRpgSystems() {
        List<RpgSystemsDTO> rpgSystemsList = rpgSystemsRepository.findAll().stream()
                .map(rpgSystems -> new RpgSystemsDTO(
                        rpgSystems.getId(),
                        rpgSystems.getName(),
                        rpgSystems.getDescription()
                )).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Systemy zostały pobrane.");
        response.put("rpgSystems", rpgSystemsList);
        return response;
    }

    public Map<String, Object> createRpgSystems(RpgSystems rpgSystems) {

        // Sprawdzenie, czy obiekt nie jest pusty
        if (rpgSystems == null || rpgSystems.getName() == null || rpgSystems.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa systemu jest wymagana.");
        }

        String name = rpgSystems.getName().trim(); // Usuwamy zbędne spacje
        // Sprawdzamy, czy nazwa systemu nie jest pusta
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Niewłaściwa nazwa systemu");
        }

        // Sprawdzamy, czy nazwa systemu nie jest zbyt długa
        if (name.length() > 255) {
            throw new IllegalArgumentException("Nazwa systemu nie może mieć więcej niż 255 znaków.");
        }

        String description = rpgSystems.getDescription().trim();
        // Sprawdzamy, czy opis systemu nie jest pusty
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Opis systemu jest wymagany.");
        }

        // Sprawdzamy, czy opis systemu nie jest zbyt długi
        if (description.length() > 1000) {
            throw new IllegalArgumentException("Opis systemu nie może mieć więcej niż 1000 znaków.");
        }

        // Sprawdzanie, czy system o podanej nazwie już istnieje
        if(rpgSystemsRepository.existsByName(name)) {
            throw new IllegalStateException("System o nazwie " + name + " już istnieje");
        }

        rpgSystems.setName(name);
        rpgSystems.setDescription(description);
        rpgSystemsRepository.save(rpgSystems);

        return CustomReturnables.getOkResponseMap("System został dodany");
    }
}
