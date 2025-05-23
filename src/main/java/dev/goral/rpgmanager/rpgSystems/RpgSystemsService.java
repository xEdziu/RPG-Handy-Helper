package dev.goral.rpgmanager.rpgSystems;

import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RpgSystemsService {

    private final RpgSystemsRepository rpgSystemsRepository;

    public RpgSystemsDTO getRpgSystemsById(Long rpgSystemsId) {
        RpgSystems rpgSystems = rpgSystemsRepository.findById(rpgSystemsId)
                .orElseThrow(() -> new ResourceNotFoundException("System o id " + rpgSystemsId + " nie istnieje"));
        return new RpgSystemsDTO(rpgSystems.getId(), rpgSystems.getName(), rpgSystems.getDescription());
    }

    public List<RpgSystemsDTO> getAllRpgSystems() {
        return rpgSystemsRepository.findAll()
                .stream()
                .map(rpgSystems -> new RpgSystemsDTO(
                        rpgSystems.getId(),
                        rpgSystems.getName(),
                        rpgSystems.getDescription()
                ))
                .collect(Collectors.toList());
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
