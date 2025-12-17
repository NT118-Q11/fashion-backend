package NT5118.Q11_backend.fashion.auth.service;

import NT5118.Q11_backend.fashion.auth.dto.GoogleOAuth2UserInfo;
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

    /**
     * Register or update a user based on OAuth2 (Google) info
     * @param info Google user information
     * @return the created or updated user
     */
    User registerOAuthUser(GoogleOAuth2UserInfo info);
}
