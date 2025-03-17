package dev.goral.rpgmanager.cp_red_classes;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authorized/cp/classes")
public class CpRedClassesController {
    private final CpRedClassesService cpRedClassesService;


}
