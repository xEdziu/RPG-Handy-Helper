package dev.goral.rpgmanager.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override

    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request);

        if (token != null){
            if (tokenProvider.validateToken(token)) {
                Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                // Jeśli token jest nieprawidłowy, ustawiamy status 401 Unauthorized
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                String errorJson = String.format(
                        "{\"error\":\"Nieprawidłowy lub wygasły token JWT\",\"message\":\"Dostęp do tego zasobu wymaga poprawnego tokenu JWT.\",\"status\":401,\"timestamp\":\"%s\",\"path\":\"%s\"}",
                        new Timestamp(new Date().getTime()), request.getRequestURI()
                );
                response.getWriter().write(errorJson);
                return; // Przerywamy dalsze przetwarzanie
            }
        }
        filterChain.doFilter(request, response);
    }
}