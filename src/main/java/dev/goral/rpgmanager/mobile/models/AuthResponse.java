package dev.goral.rpgmanager.mobile.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private long expiresIn;
}