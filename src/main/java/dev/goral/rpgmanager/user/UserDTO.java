package dev.goral.rpgmanager.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    public UserDTO(String username, String firstName, String surname, String email) {
        this.username = username;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
    }

    private String username;
    private String firstName;
    private String surname;
    private String email;
    private String password;
}
