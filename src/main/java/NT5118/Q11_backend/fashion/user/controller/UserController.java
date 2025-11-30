package NT5118.Q11_backend.fashion.user.controller;

import NT5118.Q11_backend.fashion.user.model.User;
import NT5118.Q11_backend.fashion.user.service.UserService;
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

    // User Address endpoints
    @GetMapping("/{id}/address")
    public ResponseEntity<?> getUserAddress(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(Map.of("user_address", user.getUser_address() != null ? user.getUser_address() : "")))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<?> createUserAddress(@PathVariable String id, @RequestBody Map<String, String> payload) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setUser_address(payload.get("user_address"));
                    User updated = userService.updateUser(user);
                    return ResponseEntity.ok(Map.of("message", "User address created successfully", "user_address", updated.getUser_address()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<?> updateUserAddress(@PathVariable String id, @RequestBody Map<String, String> payload) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setUser_address(payload.get("user_address"));
                    User updated = userService.updateUser(user);
                    return ResponseEntity.ok(Map.of("message", "User address updated successfully", "user_address", updated.getUser_address()));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
