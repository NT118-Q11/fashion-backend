package NT5118.Q11_backend.fashion.auth.controller;

import NT5118.Q11_backend.fashion.auth.dto.ForgotPasswordRequest;
import NT5118.Q11_backend.fashion.auth.dto.ResetPasswordRequest;
import NT5118.Q11_backend.fashion.auth.dto.VerifyOtpRequest;
import NT5118.Q11_backend.fashion.auth.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/password")
@Validated
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    /**
     * Step 1: Request password reset - sends OTP to email
     * POST /api/auth/password/forgot
     */
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        boolean success = passwordResetService.initiatePasswordReset(request.getEmail());

        if (success) {
            return ResponseEntity.ok(Map.of(
                "message", "If an account exists with this email, you will receive an OTP shortly.",
                "email", request.getEmail()
            ));
        } else {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to send OTP. Please try again later."
            ));
        }
    }

    /**
     * Step 2 (Optional): Verify OTP without resetting password
     * POST /api/auth/password/verify-otp
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        boolean valid = passwordResetService.verifyOtp(request.getEmail(), request.getOtp());

        if (valid) {
            return ResponseEntity.ok(Map.of(
                "message", "OTP is valid",
                "valid", true
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Invalid or expired OTP",
                "valid", false
            ));
        }
    }

    /**
     * Step 3: Reset password with OTP
     * POST /api/auth/password/reset
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        boolean success = passwordResetService.resetPassword(
            request.getEmail(),
            request.getOtp(),
            request.getNewPassword()
        );

        if (success) {
            return ResponseEntity.ok(Map.of(
                "message", "Password reset successfully. You can now login with your new password."
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to reset password. Invalid or expired OTP."
            ));
        }
    }
}

