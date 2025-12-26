package NT5118.Q11_backend.fashion.auth.service;

public interface EmailService {
    /**
     * Send OTP email for password reset
     * @param to recipient email
     * @param otp the OTP code
     */
    void sendPasswordResetOtp(String to, String otp);
}
