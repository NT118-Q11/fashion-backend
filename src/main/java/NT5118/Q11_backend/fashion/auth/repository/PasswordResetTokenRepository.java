package NT5118.Q11_backend.fashion.auth.repository;

import NT5118.Q11_backend.fashion.auth.model.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByEmailAndOtpAndUsedFalse(String email, String otp);
    List<PasswordResetToken> findByEmailAndUsedFalse(String email);
    void deleteByExpiryDateBefore(LocalDateTime dateTime);
    void deleteByEmail(String email);
}

