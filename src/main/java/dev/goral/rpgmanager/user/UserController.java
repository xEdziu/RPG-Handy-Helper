package dev.goral.rpgmanager.user;

import dev.goral.rpgmanager.user.additional.PasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/user/setUserPhotoPath")
    public Map<String, Object> setUserPhoto(@RequestBody String userPhotoPath) {
        return userService.setUserPhotoPath(userPhotoPath);
    }

    @PutMapping("/user/update")
    public Map<String, Object> updateProfile(@RequestBody UserUpdateRequest updateRequest, @AuthenticationPrincipal Object user) {
        return userService.updateProfile(updateRequest, user);
    }

    @PostMapping("/user/photo")
    public Map<String, Object> uploadProfilePhoto(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Object user) {
        return userService.uploadUserPhoto(file, user);
    }

    @GetMapping("/user/photo/{filename:.+}")
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable String filename) throws IOException {
        return userService.getUserPhoto(filename);
    }

    @GetMapping("/admin/user/all")
    public Map<String, Object> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/admin/user/create")
    public Map<String, Object> createUserAdmin(@RequestBody User user) {
        return userService.createUserAdmin(user);
    }


}
