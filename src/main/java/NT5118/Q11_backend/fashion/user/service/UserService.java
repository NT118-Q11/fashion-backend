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

    /**
     * Update user's name
     * @param id User ID
     * @param firstName First name
     * @param lastName Last name
     * @return Updated user
     */
    User updateUserName(String id, String firstName, String lastName);

    /**
     * Update user's email
     * @param id User ID
     * @param email New email
     * @return Updated user
     */
    User updateUserEmail(String id, String email);

    /**
     * Update user's phone number
     * @param id User ID
     * @param phoneNumber New phone number
     * @return Updated user
     */
    User updateUserPhone(String id, String phoneNumber);

    /**
     * Update user's password
     * @param id User ID
     * @param oldPassword Old password
     * @param newPassword New password
     * @return Updated user
     */
    User updateUserPassword(String id, String oldPassword, String newPassword);
}

