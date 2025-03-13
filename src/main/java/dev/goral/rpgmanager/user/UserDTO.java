package dev.goral.rpgmanager.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String firstName;
    private String surname;
    private String email;
}
