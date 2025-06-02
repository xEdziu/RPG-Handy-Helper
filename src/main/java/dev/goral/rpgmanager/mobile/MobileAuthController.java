package dev.goral.rpgmanager.mobile;

import dev.goral.rpgmanager.config.jwt.JwtTokenProvider;
import dev.goral.rpgmanager.mobile.models.AuthRequest;
import dev.goral.rpgmanager.mobile.models.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mobile/v1/auth")
@RequiredArgsConstructor
public class MobileAuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String token = tokenProvider.createToken(auth.getName(), roles);
        return ResponseEntity.ok(new AuthResponse(token, tokenProvider.getValidityInMs()));
    }
}