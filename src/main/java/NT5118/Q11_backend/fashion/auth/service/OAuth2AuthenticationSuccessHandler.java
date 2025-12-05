package NT5118.Q11_backend.fashion.auth.service;

import NT5118.Q11_backend.fashion.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    public OAuth2AuthenticationSuccessHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // authentication.getPrincipal() is an OAuth2User - we use authService to finalize processing if needed
        // For simplicity, redirect to a frontend URL. Optionally, you may generate a JWT and include it in redirect.
        String targetUrl = "http://localhost:3000/oauth2/success";
        response.sendRedirect(targetUrl);
    }
}

