package dev.goral.rpgmanager.rpgSystems.cpRed.manual.classes;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedClassesController {
    private CpRedClassesService cpRedClassesService;

    // ============ User methods ============
    // Pobierz wszystkie klasy
    @GetMapping(path = "/rpgSystems/cpRed/classes/all")
    public List<CpRedClassesDTO> getAllClasses() {
        return cpRedClassesService.getAllClasses();
    }
    // Pobierz klasę po id
    @GetMapping(path = "/rpgSystems/cpRed/classes/{classId}")
    public CpRedClassesDTO getClassById(@PathVariable("classId") Long classId) {
        return cpRedClassesService.getClassById(classId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie klasy dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/classes/all")
    public List<CpRedClasses> getAllClassesForAdmin() {
        return cpRedClassesService.getAllClassesForAdmin();
    }
    // Dodać klase
    @PostMapping(path = "/admin/rpgSystems/cpRed/classes/add")
    public Map<String, Object> addClass(@RequestBody CpRedClasses cpRedClasses) {
        return cpRedClassesService.addClass(cpRedClasses);
    }
    // Modyfikować klasę
    @PutMapping(path = "/admin/rpgSystems/cpRed/classes/update/{classId}")
    public Map<String, Object> updateClass(@PathVariable("classId") Long classId, @RequestBody CpRedClasses cpRedClasses) {
        return cpRedClassesService.updateClass(classId, cpRedClasses);
    }

}
