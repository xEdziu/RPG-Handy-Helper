package dev.goral.rpgmanager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/authorized")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user")
    public List<String> getUserInfo() {
        return new ArrayList<>(
                List.of(
                        "Janek",
                        "Kowalski",
                        "Kijowska 2"
                )
        );
    }
}
