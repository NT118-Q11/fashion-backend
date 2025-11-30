package NT5118.Q11_backend.fashion.auth.controller;

import NT5118.Q11_backend.fashion.auth.dto.UserRegistrationRequest;
import NT5118.Q11_backend.fashion.auth.dto.UserLoginRequest;
import NT5118.Q11_backend.fashion.auth.service.AuthService;
import NT5118.Q11_backend.fashion.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
}
