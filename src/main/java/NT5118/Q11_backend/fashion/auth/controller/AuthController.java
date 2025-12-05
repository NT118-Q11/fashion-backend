package NT5118.Q11_backend.fashion.auth.controller;

import NT5118.Q11_backend.fashion.auth.dto.UserRegistrationRequest;
import NT5118.Q11_backend.fashion.auth.dto.UserLoginRequest;
import NT5118.Q11_backend.fashion.auth.dto.GoogleOAuth2UserInfo;
import NT5118.Q11_backend.fashion.auth.service.AuthService;
import NT5118.Q11_backend.fashion.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Create a new user account with username, email, and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Email or username already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = authService.register(request);
        return ResponseEntity.status(201).body(java.util.Map.of(
                "message", "User registered successfully",
                "user", java.util.Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "phone_number", user.getPhone_number() != null ? user.getPhone_number() : "",
                        "user_address", user.getUser_address() != null ? user.getUser_address() : ""
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
                        "phone_number", user.getPhone_number() != null ? user.getPhone_number() : "",
                        "user_address", user.getUser_address() != null ? user.getUser_address() : ""
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
                        "phone_number", user.getPhone_number() != null ? user.getPhone_number() : "",
                        "user_address", user.getUser_address() != null ? user.getUser_address() : ""
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
                        "phone_number", user.getPhone_number() != null ? user.getPhone_number() : "",
                        "user_address", user.getUser_address() != null ? user.getUser_address() : ""
                )
        ));
    }
}
