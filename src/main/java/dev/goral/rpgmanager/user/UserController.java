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
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(
                userDTO.getUsername(),
                userDTO.getFirstName(),
                userDTO.getSurname(),
                userDTO.getEmail(),
                userDTO.getPassword()
        );
    }

    @GetMapping("/confirmEmail")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        return userService.confirmEmail(token);
    }

    @GetMapping("/authorized/user")
    public UserDTO getAuthorizedUser() {
        return userService.getAuthorizedUser();
    }
}
