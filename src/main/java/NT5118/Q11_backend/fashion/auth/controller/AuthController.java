package NT5118.Q11_backend.fashion.auth.controller;

import NT5118.Q11_backend.fashion.auth.dto.UserRegistrationRequest;
import NT5118.Q11_backend.fashion.auth.dto.UserLoginRequest;
import NT5118.Q11_backend.fashion.auth.dto.GoogleOAuth2UserInfo;
import NT5118.Q11_backend.fashion.auth.service.AuthService;
import NT5118.Q11_backend.fashion.auth.service.GoogleOAuth2Service;
import NT5118.Q11_backend.fashion.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;
    private final GoogleOAuth2Service googleOAuth2Service;

    public AuthController(AuthService authService, GoogleOAuth2Service googleOAuth2Service) {
        this.authService = authService;
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = authService.register(request);
        return ResponseEntity.status(201).body(java.util.Map.of(
                "message", "User registered successfully",
                "user", java.util.Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "phone_number", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                        "user_address", user.getUserAddress() != null ? user.getUserAddress() : ""
                )
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) {
        User user = authService.login(request);
        return ResponseEntity.ok(java.util.Map.of(
                "message", "Login successful",
                "user", java.util.Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "phone_number", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                        "user_address", user.getUserAddress() != null ? user.getUserAddress() : ""
                )
        ));
    }

    // Google register (accepts Google user info payload)
    @PostMapping("/register-gmail")
    public ResponseEntity<?> registerWithGoogle(@RequestBody GoogleOAuth2UserInfo info) {
        User user = authService.registerOAuthUser(info);
        return ResponseEntity.status(201).body(java.util.Map.of(
                "message", "User registered with Google successfully",
                "user", java.util.Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "phone_number", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                        "user_address", user.getUserAddress() != null ? user.getUserAddress() : ""
                )
        ));
    }

    // Google login (accepts Google user info payload) - will return existing or created user
    @PostMapping("/login-gmail")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleOAuth2UserInfo info) {
        User user = authService.registerOAuthUser(info);
        return ResponseEntity.ok(java.util.Map.of(
                "message", "Login with Google successful",
                "user", java.util.Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "phone_number", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                        "user_address", user.getUserAddress() != null ? user.getUserAddress() : ""
                )
        ));
    }

    /**
     * GET endpoint - Redirect user to Google OAuth2 consent screen
     * Example: GET http://localhost:8080/api/auth/oauth2/google
     */
    @GetMapping("/oauth2/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        // Generate random state for CSRF protection
        String state = UUID.randomUUID().toString();
        // TODO: Store state in session/redis to verify in callback

        String authUrl = googleOAuth2Service.getAuthorizationUrl(state);
        response.sendRedirect(authUrl);
    }

    /**
     * GET endpoint - Callback from Google with authorization code
     * Google will redirect to: GET http://localhost:8080/api/auth/oauth2/callback/google?code=...&state=...
     */
    @GetMapping("/oauth2/callback/google")
    public void handleGoogleCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            HttpServletResponse response
    ) throws IOException {
        // Handle error from Google
        if (error != null) {
            response.sendRedirect("http://localhost:3000/login?error=" + error);
            return;
        }

        try {
            // TODO: Verify state parameter matches stored state (CSRF protection)

            // Step 1: Exchange authorization code for access token
            GoogleOAuth2Service.GoogleTokenResponse tokenResponse =
                    googleOAuth2Service.exchangeCodeForToken(code);

            // Step 2: Get user info from Google using access token
            GoogleOAuth2Service.GoogleUserInfo userInfo =
                    googleOAuth2Service.getUserInfo(tokenResponse.getAccessToken());

            // Step 3: Create or login user in our system
            GoogleOAuth2UserInfo oauthInfo = new GoogleOAuth2UserInfo();
            oauthInfo.setId(userInfo.getId());
            oauthInfo.setEmail(userInfo.getEmail());
            oauthInfo.setName(userInfo.getName());
            oauthInfo.setPicture(userInfo.getPicture());

            // --- IMPORTANT: pass the ID token (id_token) to the backend verifier ---
            // The tokenResponse contains both an access token and an id_token. The
            // backend's GoogleIdTokenVerifier expects the ID token (JWT) to verify
            // the user's identity and audience. Set it on the DTO so registerOAuthUser
            // can verify it.
            oauthInfo.setAccessToken(tokenResponse.getIdToken());

            User user = authService.registerOAuthUser(oauthInfo);

            // Step 4: Generate JWT token (TODO: implement JWT service)
            // String jwtToken = jwtService.generateToken(user);

            // Step 5: Redirect to frontend with user info or JWT
            // For now, redirect with user ID (in production, use JWT in query or cookie)
            response.sendRedirect("http://localhost:3000/oauth2/success?userId=" + user.getId() +
                                "&email=" + user.getEmail());

        } catch (Exception e) {
            // Log error and redirect to frontend error page
            e.printStackTrace();
            response.sendRedirect("http://localhost:3000/login?error=oauth_failed");
        }
    }
}
