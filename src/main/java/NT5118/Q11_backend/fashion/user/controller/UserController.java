package NT5118.Q11_backend.fashion.user.controller;

import NT5118.Q11_backend.fashion.user.dto.UpdateEmailRequest;
import NT5118.Q11_backend.fashion.user.dto.UpdateNameRequest;
import NT5118.Q11_backend.fashion.user.dto.UpdatePasswordRequest;
import NT5118.Q11_backend.fashion.user.dto.UpdatePhoneRequest;
import NT5118.Q11_backend.fashion.user.model.User;
import NT5118.Q11_backend.fashion.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.getUserById(id)
                .map(existingUser -> {
                    user.setId(id);
                    User updated = userService.updateUser(user);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // Update user name
    @PutMapping("/{id}/name")
    public ResponseEntity<?> updateUserName(@PathVariable String id, @Valid @RequestBody UpdateNameRequest request) {
        try {
            User updated = userService.updateUserName(id, request.getFirstName(), request.getLastName());
            return ResponseEntity.ok(Map.of(
                    "message", "User name updated successfully",
                    "firstName", updated.getFirstName(),
                    "lastName", updated.getLastName()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update user email
    @PutMapping("/{id}/email")
    public ResponseEntity<?> updateUserEmail(@PathVariable String id, @Valid @RequestBody UpdateEmailRequest request) {
        try {
            User updated = userService.updateUserEmail(id, request.getEmail());
            return ResponseEntity.ok(Map.of(
                    "message", "User email updated successfully",
                    "email", updated.getEmail()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update user phone number
    @PutMapping("/{id}/phone")
    public ResponseEntity<?> updateUserPhone(@PathVariable String id, @Valid @RequestBody UpdatePhoneRequest request) {
        try {
            User updated = userService.updateUserPhone(id, request.getPhoneNumber());
            return ResponseEntity.ok(Map.of(
                    "message", "User phone number updated successfully",
                    "phoneNumber", updated.getPhoneNumber()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update user password
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable String id, @Valid @RequestBody UpdatePasswordRequest request) {
        try {
            userService.updateUserPassword(id, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "User password updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // User Address endpoints
    @GetMapping("/{id}/address")
    public ResponseEntity<?> getUserAddress(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(Map.of("user_address", user.getUserAddress() != null ? user.getUserAddress() : "")))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<?> createUserAddress(@PathVariable String id, @RequestBody Map<String, String> payload) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setUserAddress(payload.get("user_address"));
                    User updated = userService.updateUser(user);
                    return ResponseEntity.ok(Map.of("message", "User address created successfully", "user_address", updated.getUserAddress()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<?> updateUserAddress(@PathVariable String id, @RequestBody Map<String, String> payload) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setUserAddress(payload.get("user_address"));
                    User updated = userService.updateUser(user);
                    return ResponseEntity.ok(Map.of("message", "User address updated successfully", "user_address", updated.getUserAddress()));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
