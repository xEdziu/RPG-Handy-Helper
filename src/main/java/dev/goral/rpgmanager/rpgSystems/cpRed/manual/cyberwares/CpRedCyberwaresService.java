package dev.goral.rpgmanager.rpgSystems.cpRed.manual.cyberwares;

import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CpRedCyberwaresService {
    private final CpRedCyberwaresRepository cpRedCyberwaresRepository;

    // Pobierz wszystkie cyberware
    public List<CpRedCyberwaresDTO> getAllCyberwares() {
        List<CpRedCyberwares> cpRedCyberwaresList= cpRedCyberwaresRepository.findAll();
        return cpRedCyberwaresList.stream().map(
                cpRedCyberwares-> new CpRedCyberwaresDTO(
                cpRedCyberwares.getName(),
                cpRedCyberwares.getMountPlace().toString(),
                cpRedCyberwares.getRequirements(),
                cpRedCyberwares.getHumanityLoss(),
                cpRedCyberwares.getSize(),
                cpRedCyberwares.getInstallationPlace().toString(),
                cpRedCyberwares.getPrice(),
                cpRedCyberwares.getAvailability().toString(),
                cpRedCyberwares.getDescription()
        )).toList();
    }
    // Pobierz cyberware po id
    public CpRedCyberwaresDTO getCyberwareById(Long cyberwareId) {
        return cpRedCyberwaresRepository.findById(cyberwareId).map(
                cpRedCyberwares -> new CpRedCyberwaresDTO(
                        cpRedCyberwares.getName(),
                        cpRedCyberwares.getMountPlace().toString(),
                        cpRedCyberwares.getRequirements(),
                        cpRedCyberwares.getHumanityLoss(),
                        cpRedCyberwares.getSize(),
                        cpRedCyberwares.getInstallationPlace().toString(),
                        cpRedCyberwares.getPrice(),
                        cpRedCyberwares.getAvailability().toString(),
                        cpRedCyberwares.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Wszczep o id " +cyberwareId + " nie istnieje"));
    }
    // Pobierz wszystkie cyberware dla admina
    public List<CpRedCyberwares> getAllCyberwaresForAdmin() {
        return cpRedCyberwaresRepository.findAll();
    }
//
//    // Dodać cyberware
//    public Map<String, Object> addCyberware(CpRedCyberwares cpRedCyberwares) {
//
//    }
//
//    // Modyfikować cyberware
//    public Map<String, Object> updateCyberware(Long cyberwareId, CpRedCyberwares cpRedCyberwares) {
//
//    }
}
