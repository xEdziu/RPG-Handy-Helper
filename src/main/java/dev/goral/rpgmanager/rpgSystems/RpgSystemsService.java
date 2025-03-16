package dev.goral.rpgmanager.rpgSystems;

import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RpgSystemsService {

    private RpgSystemsRepository rpgSystemsRepository;

    @Autowired
    public RpgSystemsService(RpgSystemsRepository rpgSystemsRepository) {
        this.rpgSystemsRepository = rpgSystemsRepository;
    }

    public RpgSystemsDTO getrpgSystemsById(Long rpgSystemsId) {
        RpgSystems rpgSystems = rpgSystemsRepository.findById(rpgSystemsId)
                .orElseThrow(() -> new ResourceNotFoundException("rpgSystems with id " + rpgSystemsId + " does not exist"));
        return new RpgSystemsDTO(rpgSystems.getId(), rpgSystems.getName(), rpgSystems.getDescription());
    }

    public List<RpgSystemsDTO> getAllrpgSystems() {
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
        return Map.of("message", "Rpg system created successfully");
    }
}
