package dev.goral.rpgmanager.security;

import dev.goral.rpgmanager.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(customizer ->
                        customizer
                                .requestMatchers("/api/v1/authorized/admin/**").hasAuthority("ROLE_ADMIN") // Only for ADMIN
                                .requestMatchers("/api/v1/authorized/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") // For USER and ADMIN
                                .requestMatchers("/api/v1/**").permitAll() // Publicly accessible, no login required
                                .requestMatchers("/admin", "/admin/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/home", "/home/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/login", "/api/v1/**", "/register", "/activate").permitAll()
                                .requestMatchers("/forgotPassword", "/resetPassword").permitAll()
                                .requestMatchers("/", "/index.html", "/static/**", "/resources/**").permitAll() // Allow access to static resources
                                .requestMatchers("/actuator/health").permitAll() // Allow access to health check
                                .requestMatchers("/styles/**", "/scripts/**", "/img/**", "/fonts/**").permitAll() // Allow access to static resources
                                .anyRequest()
                                .authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/logout") // Wyłączenie CSRF tylko dla logout
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .formLogin(httpConfig -> httpConfig
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(customAuthenticationSuccessHandler) // Custom success handler
                        .failureUrl("/login?error=true")
                        .failureHandler((_, response, exception) -> {
                            if (exception.getMessage().equals("User is disabled")) {
                                response.sendRedirect("/login?error=disabled");
                            } else {
                                response.sendRedirect("/login?error=true");
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/login?error=logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Strona logowania
                        .defaultSuccessUrl("/home", true) // Po zalogowaniu przekierowuje na /home
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Nasz serwis obsługujący użytkowników Discord
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterAfter(new CsrfTokenGeneratingFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

}
