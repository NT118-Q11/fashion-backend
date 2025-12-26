package NT5118.Q11_backend.fashion.auth.service;

public interface PasswordResetService {
    /**
     * Initiate password reset process - generates OTP and sends email
     * @param email user's email
     * @return true if OTP sent successfully
     */
    boolean initiatePasswordReset(String email);

    /**
     * Verify OTP without resetting password
     * @param email user's email
     * @param otp the OTP to verify
     * @return true if OTP is valid
     */
    boolean verifyOtp(String email, String otp);

    /**
     * Reset password using OTP
     * @param email user's email
     * @param otp the OTP
     * @param newPassword new password
     * @return true if password reset successfully
     */
    boolean resetPassword(String email, String otp, String newPassword);
}

