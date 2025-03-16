package dev.goral.rpgmanager.rpgSystems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
public class RpgSystemsController {

    private final RpgSystemsService rpgSystemsService;

    @Autowired
    public RpgSystemsController(RpgSystemsService rpgSystemsService) {
        this.rpgSystemsService = rpgSystemsService;
    }

    // ============ User methods ============

    @GetMapping(path = "/rpgSystems/{rpgSystemsId}")
    public RpgSystemsDTO getrpgSystemsById(@PathVariable("rpgSystemsId") Long rpgSystemsId) {
        return rpgSystemsService.getrpgSystemsById(rpgSystemsId);
    }

    @GetMapping(path = "/rpgSystems/all")
    public List<RpgSystemsDTO> getAllrpgSystems() {
        return rpgSystemsService.getAllrpgSystems();
    }

    // ============ Admin methods ============

    @PostMapping(path = "/admin/rpgSystems/create")
    public Map<String, Object> createRpgSystems(@RequestBody RpgSystems rpgSystems) {
        return rpgSystemsService.createRpgSystems(rpgSystems);
    }
}
