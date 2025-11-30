package NT5118.Q11_backend.fashion.user.service;

import NT5118.Q11_backend.fashion.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Get user by ID
     * @param id User ID
     * @return User if found
     */
    Optional<User> getUserById(String id);

    /**
     * Get user by email
     * @param email User email
     * @return User if found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Get all users
     * @return List of users
     */
    List<User> getAllUsers();

    /**
     * Update user
     * @param user User to update
     * @return Updated user
     */
    User updateUser(User user);

    /**
     * Delete user by ID
     * @param id User ID
     */
    void deleteUser(String id);
}

