package dev.goral.rpgmanager.mobile;

import dev.goral.rpgmanager.user.UserDTO;
import dev.goral.rpgmanager.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/mobile/v1/authorized")
public class MobileUserController {

    UserService userService;

    @GetMapping("/user")
    public UserDTO getAuthorizedUser() {
        return userService.getAuthorizedUser();
    }
}
