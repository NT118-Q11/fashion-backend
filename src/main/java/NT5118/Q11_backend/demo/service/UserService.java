package NT5118.Q11_backend.demo.service;

import NT5118.Q11_backend.demo.dto.UserRegistrationRequest;
import NT5118.Q11_backend.demo.model.User;

public interface UserService {
    User register(UserRegistrationRequest request);
}
