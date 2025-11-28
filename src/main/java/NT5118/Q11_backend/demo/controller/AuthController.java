package NT5118.Q11_backend.demo.controller;

import NT5118.Q11_backend.demo.dto.UserRegistrationRequest;
import NT5118.Q11_backend.demo.model.User;
import NT5118.Q11_backend.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = userService.register(request);
        return ResponseEntity.status(201).body(
                java.util.Collections.singletonMap("message", "User registered successfully")
        );
    }
}
