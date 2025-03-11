package dev.goral.rpgmanager.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authorized")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping("/user")
    public UserDTO getAuthorizedUser() {
        return userService.getAuthorizedUser();
    }
}
