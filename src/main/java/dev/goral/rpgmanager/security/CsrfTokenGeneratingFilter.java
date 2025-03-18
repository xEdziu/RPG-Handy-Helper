package dev.goral.rpgmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


public class CsrfTokenGeneratingFilter extends OncePerRequestFilter {
    private final CsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = csrfTokenRepository.loadToken(request);

        if (csrfToken == null) {
            // Jeśli token CSRF nie istnieje, generujemy nowy
            csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);
            System.out.println("✅ Wygenerowano nowy token CSRF: " + csrfToken.getToken());
        } else {
            System.out.println("🔹 Istniejący token CSRF: " + csrfToken.getToken());
        }

        // Przekazanie żądania dalej w łańcuchu filtrów
        filterChain.doFilter(request, response);
    }
}

