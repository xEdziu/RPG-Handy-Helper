package dev.goral.rpgmanager.rpgSystems.cpRed.characters.classes;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedClassesController {
    private CpRedClassesService cpRedClassesService;

    // ============ User methods ============
    // Pobierz wszystkie klasy
    @GetMapping(path = "/rpgSystems/cpRed/classes/all")
    public CpRedClassesDTO getAllClasses() {
        return cpRedClassesService.getAllClasses();
    }
    // Pobierz klasę po id
    @GetMapping(path = "/rpgSystems/cpRed/classes/{classId}")
    public CpRedClassesDTO getClassById(Long classId) {
        return cpRedClassesService.getClassById(classId);
    }

    // ============ Admin methods ============
    // Dodać klase
    @PostMapping(path = "/admin/rpgSystems/cpRed/classes/add")
    public CpRedClassesDTO addClass(CpRedClassesDTO cpRedClassesDTO) {
        return cpRedClassesService.addClass(cpRedClassesDTO);
    }
    // Modyfikować klasę
    @PutMapping(path = "/admin/rpgSystems/cpRed/classes/update/{classId}")
    public CpRedClassesDTO updateClass(@PathVariable("classId") Long classId, @RequestBody CpRedClassesDTO cpRedClassesDTO) {
        return cpRedClassesService.updateClass(classId, cpRedClassesDTO);
    }

}
