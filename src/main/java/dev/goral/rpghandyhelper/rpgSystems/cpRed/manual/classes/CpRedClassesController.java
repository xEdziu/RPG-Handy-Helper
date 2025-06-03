package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.classes;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedClassesController {
    private CpRedClassesService cpRedClassesService;


    @GetMapping(path = "/rpgSystems/cpRed/classes/all")
    public Map<String, Object> getAllClasses() {
        return cpRedClassesService.getAllClasses();
    }

   @GetMapping(path = "/rpgSystems/cpRed/classes/{classId}")
   public Map<String, Object> getClassById(@PathVariable("classId") Long classId) {
        return cpRedClassesService.getClassById(classId);
    }


    @GetMapping(path = "/admin/rpgSystems/cpRed/classes/all")
    public Map<String, Object> getAllClassesForAdmin() {
        return cpRedClassesService.getAllClassesForAdmin();
    }

    @PostMapping(path = "/admin/rpgSystems/cpRed/classes/add")
    public Map<String, Object> addClass(@RequestBody CpRedClasses cpRedClasses) {
        return cpRedClassesService.addClass(cpRedClasses);
    }

    @PutMapping(path = "/admin/rpgSystems/cpRed/classes/update/{classId}")
    public Map<String, Object> updateClass(@PathVariable("classId") Long classId, @RequestBody CpRedClasses cpRedClasses) {
        return cpRedClassesService.updateClass(classId, cpRedClasses);
    }

}
