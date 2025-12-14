package NT5118.Q11_backend.fashion.common.config;

import NT5118.Q11_backend.fashion.auth.service.GoogleOAuth2UserService;
import NT5118.Q11_backend.fashion.auth.service.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GoogleOAuth2UserService googleOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    public SecurityConfig(GoogleOAuth2UserService googleOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler) {
        this.googleOAuth2UserService = googleOAuth2UserService;
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for public endpoints (register, login, oauth2)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/register", "/api/auth/login",
                                "/api/auth/oauth2/**", "/api/auth/register-gmail", "/api/auth/login-gmail")
                )
                // Configure authorization
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register-gmail", "/api/auth/login-gmail").permitAll()
                        .requestMatchers("/api/auth/oauth2/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/**", "/oauth2/authorization/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Enable OAuth2 login
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2UserService))
                        .successHandler(oauth2AuthenticationSuccessHandler)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
