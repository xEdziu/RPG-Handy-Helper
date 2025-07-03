package dev.goral.rpghandyhelper.user;

import dev.goral.rpghandyhelper.user.additional.PasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authorized")
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public UserDTO getAuthorizedUser() {
        return userService.getAuthorizedUser();
    }

    @GetMapping("/user/isPasswordSetForDiscordUser")
    public Map<String, Object> isPasswordSetForDiscordUser(@AuthenticationPrincipal Object user) {
        return userService.isPasswordSetForDiscordUser(user);
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

    @GetMapping("/user/photo")
    public Map<String, Object> getUserActualPhotoPath() {
        return userService.getUserActualPhotoPath();
    }

    @GetMapping("/user/photo/username/{username}")
    public Map<String, Object> getUserPhotoByUsername(@PathVariable String username) {
        return userService.getUserPhotoByUsername(username);
    }
  
    @GetMapping("/user/photo/defaults")
    public Map<String, Object> getDefaultUserPhotos() {
        return userService.getDefaultProfilePics();
    }

    // Admin endpoints

    @GetMapping("/admin/user/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        return userService.getAdminUserById(id);
    }

    @GetMapping("/admin/user/all")
    public Map<String, Object> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/admin/user/create")
    public Map<String, Object> createUserAdmin(@RequestBody User user) {
        return userService.createUserAdmin(user);
    }

    @PutMapping("/admin/user/update/{id}")
    public Map<String, Object> updateUserAdmin(@PathVariable Long id, @RequestBody UserUpdateAdminRequest updateRequest) {
        return userService.updateUserAdmin(id, updateRequest);
    }

    @PutMapping("/admin/user/changePassword/{id}")
    public Map<String, Object> changeUserPasswordAdmin(@PathVariable Long id, @RequestBody PasswordRequest passwordRequest) {
        return userService.changeUserPasswordAdmin(id, passwordRequest.getPassword());
    }

    @DeleteMapping("/admin/user/delete/{id}")
    public Map<String, Object> deleteUserAdmin(@PathVariable Long id) {
        return userService.deleteUserAdmin(id);
    }

}
