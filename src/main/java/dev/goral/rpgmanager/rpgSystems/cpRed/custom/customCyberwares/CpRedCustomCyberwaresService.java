package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCyberwares;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomCyberwaresService {
    private final CpRedCustomCyberwaresRepository cpRedCustomCyberwaresRepository;

    // Pobierz wszystkie cyberware
    public List<CpRedCustomCyberwaresDTO> getAllCyberware() {

    }

    // Pobierz cyberware po id
    public CpRedCustomCyberwaresDTO getCyberwareById(Long cyberwareId) {

    }

    // Doadaj cyberware
    public Map<String, Object> addCyberware(CpRedCustomCyberwares cpRedCustomCyberwares) {

    }

    // Modyfikować cyberware
    public Map<String, Object> updateCyberware(Long cyberwareId, CpRedCustomCyberwares cpRedCustomCyberwares) {

    }

    // Pobierz wszystkie cyberware dla admina
    public List<CpRedCustomCyberwares> getAllCyberwareForAdmin() {
        return cpRedCustomCyberwaresRepository.findAll();
    }
}
