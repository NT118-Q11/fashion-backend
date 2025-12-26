package NT5118.Q11_backend.fashion.auth.service.implementations;

import NT5118.Q11_backend.fashion.auth.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@fashion.com}")
    private String fromEmail;

    @Value("${app.name:Fashion Store}")
    private String appName;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetOtp(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(appName + " - Password Reset OTP");
        message.setText(buildOtpEmailContent(otp));

        mailSender.send(message);
    }

    private String buildOtpEmailContent(String otp) {
        return String.format("""
            Dear Customer,
            
            You have requested to reset your password for your %s account.
            
            Your OTP code is: %s
            
            This code will expire in 10 minutes.
            
            If you did not request this password reset, please ignore this email.
            
            Best regards,
            %s Team
            """, appName, otp, appName);
    }
}

