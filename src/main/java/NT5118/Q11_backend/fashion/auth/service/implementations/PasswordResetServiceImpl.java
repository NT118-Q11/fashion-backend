package NT5118.Q11_backend.fashion.auth.service.implementations;

import NT5118.Q11_backend.fashion.auth.model.PasswordResetToken;
import NT5118.Q11_backend.fashion.auth.repository.PasswordResetTokenRepository;
import NT5118.Q11_backend.fashion.auth.service.EmailService;
import NT5118.Q11_backend.fashion.auth.service.PasswordResetService;
import NT5118.Q11_backend.fashion.user.model.User;
import NT5118.Q11_backend.fashion.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 10;
    private static final SecureRandom random = new SecureRandom();

    public PasswordResetServiceImpl(PasswordResetTokenRepository tokenRepository,
                                   UserRepository userRepository,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public boolean initiatePasswordReset(String email) {
        // Check if user exists
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // Return true to prevent email enumeration attacks
            // but don't actually send email
            return true;
        }

        // Invalidate any existing OTPs for this email
        List<PasswordResetToken> existingTokens = tokenRepository.findByEmailAndUsedFalse(email);
        for (PasswordResetToken token : existingTokens) {
            token.setUsed(true);
            tokenRepository.save(token);
        }

        // Generate new OTP
        String otp = generateOtp();

        // Create token with expiry
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
        PasswordResetToken token = new PasswordResetToken(email, otp, expiryDate);
        tokenRepository.save(token);

        // Send email
        try {
            emailService.sendPasswordResetOtp(email, otp);
            return true;
        } catch (Exception e) {
            // Log error but don't expose to client
            System.err.println("Failed to send password reset email: " + e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyOtp(String email, String otp) {
        PasswordResetToken token = tokenRepository
            .findByEmailAndOtpAndUsedFalse(email, otp)
            .orElse(null);

        if (token == null) {
            return false;
        }

        return !token.isExpired();
    }

    @Override
    @Transactional
    public boolean resetPassword(String email, String otp, String newPassword) {
        // Find valid token
        PasswordResetToken token = tokenRepository
            .findByEmailAndOtpAndUsedFalse(email, otp)
            .orElse(null);

        if (token == null || token.isExpired()) {
            return false;
        }

        // Find user
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        token.setUsed(true);
        tokenRepository.save(token);

        return true;
    }

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}

