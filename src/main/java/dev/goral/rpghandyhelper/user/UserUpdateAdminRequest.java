package dev.goral.rpghandyhelper.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateAdminRequest {
    String username;
    String firstName;
    String surname;
    String email;
    UserRole role;
    Boolean locked;
    Boolean enabled;
    String userPhotoPath;
}
