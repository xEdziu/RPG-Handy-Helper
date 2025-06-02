package dev.goral.rpghandyhelper.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String username;
    private String firstName;
    private String surname;
    private String email;
}