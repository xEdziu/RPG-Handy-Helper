package dev.goral.rpghandyhelper.mobile;

import dev.goral.rpghandyhelper.config.jwt.JwtTokenProvider;
import dev.goral.rpghandyhelper.mobile.models.AuthRequest;
import dev.goral.rpghandyhelper.mobile.models.AuthResponse;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mobile/v1/auth")
@RequiredArgsConstructor
public class MobileAuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()
                    )
            );
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            String token = tokenProvider.createToken(auth.getName(), roles);
            Map<String, Object> response = CustomReturnables.getOkResponseMap("Poprawne logowanie");
            response.put("authResponse", new AuthResponse(token, tokenProvider.getValidityInMs()));
            return response;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Niepoprawny login lub hasło");
        }
    }
}