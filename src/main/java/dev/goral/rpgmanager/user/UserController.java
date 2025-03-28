package dev.goral.rpgmanager.user;

import dev.goral.rpgmanager.user.additional.PasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authorized")
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public UserDTO getAuthorizedUser() {
        return userService.getAuthorizedUser();
    }

    @PostMapping("/setPassword")
    public Map<String, Object> setPassword(@RequestBody PasswordRequest passwordRequest) {
        return userService.setPassword(passwordRequest.getPassword());
    }

    @PostMapping("/user/setUserPhoto")
    public Map<String, Object> setUserPhoto(@RequestBody String userPhotoPath) {
        return userService.setUserPhoto(userPhotoPath);
    }

    @GetMapping("/admin/user/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/admin/user/create")
    public Map<String, Object> createUserAdmin(@RequestBody User user) {
        return userService.createUserAdmin(user);
    }

}
