package dev.goral.rpgmanager.rpgSystems.cpRed.characters.classes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CpRedClassesService {

    private final CpRedClassesRepository cpRedClassesRepository;

    // Pobierz wszystkie klasy
    public CpRedClassesDTO getAllClasses() {
        
    }

    // Pobierz klasę po id
    public CpRedClassesDTO getClassById(Long classId) {

    }

    // Dodać klase
    public CpRedClassesDTO addClass(CpRedClassesDTO cpRedClassesDTO) {

    }

    // Modyfikować klasę
    public CpRedClassesDTO updateClass(Long classId, CpRedClassesDTO cpRedClassesDTO) {

    }
}
