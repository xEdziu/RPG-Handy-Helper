package dev.goral.rpgmanager.rpgSystems;

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
        rpgSystemsRepository.save(rpgSystems);
        return Map.of("message", "System dodany pomyślnie");
    }
}
