package NT5118.Q11_backend.fashion.auth.service;

import NT5118.Q11_backend.fashion.auth.dto.UserLoginRequest;
import NT5118.Q11_backend.fashion.auth.dto.UserRegistrationRequest;
import NT5118.Q11_backend.fashion.user.model.User;

public interface AuthService {
    /**
     * Register a new user
     * @param request User registration request
     * @return Created user
     */
    User register(UserRegistrationRequest request);

    /**
     * Login user
     * @param request User login request
     * @return User if credentials are valid
     */
    User login(UserLoginRequest request);
}
