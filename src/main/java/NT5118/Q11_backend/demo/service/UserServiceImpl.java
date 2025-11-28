package NT5118.Q11_backend.demo.service;

import NT5118.Q11_backend.demo.dto.UserRegistrationRequest;
import NT5118.Q11_backend.demo.model.User;
import NT5118.Q11_backend.demo.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }

        String hashed = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), request.getEmail(), hashed);
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            // in case uniqueness constraint hit due to race
            throw new IllegalStateException("User could not be created", ex);
        }
    }
}
