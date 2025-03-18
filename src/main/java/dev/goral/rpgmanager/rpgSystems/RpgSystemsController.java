package dev.goral.rpgmanager.rpgSystems;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class RpgSystemsController {

    private final RpgSystemsService rpgSystemsService;

    // ============ User methods ============

    @GetMapping(path = "/rpgSystems/{rpgSystemsId}")
    public RpgSystemsDTO getRpgSystemsById(@PathVariable("rpgSystemsId") Long rpgSystemsId) {
        return rpgSystemsService.getRpgSystemsById(rpgSystemsId);
    }

    @GetMapping(path = "/rpgSystems/all")
    public List<RpgSystemsDTO> getAllRpgSystems() {
        return rpgSystemsService.getAllRpgSystems();
    }

    // ============ Admin methods ============

    @PostMapping(path = "/admin/rpgSystems/create")
    public Map<String, Object> createRpgSystems(@RequestBody RpgSystems rpgSystems) {
        return rpgSystemsService.createRpgSystems(rpgSystems);
    }
}
