package dev.goral.rpgmanager.security;

import dev.goral.rpgmanager.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
                                .anyRequest()
                                .authenticated()
                )
                // .csrf(AbstractHttpConfigurer::disable)
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
                        .logoutUrl("/logout")
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
                .authenticationProvider(daoAuthenticationProvider());

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
