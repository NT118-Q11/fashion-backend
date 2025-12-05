package NT5118.Q11_backend.fashion.auth.service.implementations;

import NT5118.Q11_backend.fashion.auth.dto.GoogleOAuth2UserInfo;
import NT5118.Q11_backend.fashion.auth.dto.UserLoginRequest;
import NT5118.Q11_backend.fashion.auth.dto.UserRegistrationRequest;
import NT5118.Q11_backend.fashion.auth.service.AuthService;
import NT5118.Q11_backend.fashion.user.model.User;
import NT5118.Q11_backend.fashion.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(UserRegistrationRequest request) {
        // Validate that username must be either phone_number or email
        if (!request.getUsername().equals(request.getEmail()) &&
            !request.getUsername().equals(request.getPhone_number())) {
            throw new IllegalArgumentException("Username must be either email or phone number");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }

        String hashed = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), request.getEmail(), hashed, request.getPhone_number());
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            // in case uniqueness constraint hit due to race
            throw new IllegalStateException("User could not be created", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User login(UserLoginRequest request) {
        String username = request.getUsername();
        User user = null;

        // Try to find user by email, phone_number, or username
        if (username.contains("@")) {
            user = userRepository.findByEmail(username).orElse(null);
        } else if (username.matches("\\d{10,11}")) {
            user = userRepository.findByPhoneNumber(username).orElse(null);
        }

        // If not found by email or phone, try username
        if (user == null) {
            user = userRepository.findByUsername(username).orElse(null);
        }

        // Validate user exists and password matches
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user;
    }

    @Override
    @Transactional
    public User registerOAuthUser(GoogleOAuth2UserInfo info) {
        if (info == null || info.getEmail() == null) {
            throw new IllegalArgumentException("Invalid OAuth2 user info");
        }

        // If user exists by email, return it
        java.util.Optional<User> existing = userRepository.findByEmail(info.getEmail());
        if (existing.isPresent()) {
            return existing.get();
        }

        // Create a username from the email local-part or name
        String baseUsername = info.getEmail().split("@")[0];
        String username = baseUsername;
        int suffix = 0;
        while (userRepository.existsByUsername(username)) {
            suffix++;
            username = baseUsername + suffix;
        }

        // Create a random password placeholder (unusable) and hash it
        String randomPassword = java.util.UUID.randomUUID().toString();
        String hashed = passwordEncoder.encode(randomPassword);

        User user = new User(username, info.getEmail(), hashed, "0");
        user.setUser_address("");
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("OAuth user could not be created", ex);
        }
    }
}
