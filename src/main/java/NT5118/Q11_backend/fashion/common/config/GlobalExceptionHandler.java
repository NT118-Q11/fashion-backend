package NT5118.Q11_backend.fashion.common.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
        // Check if this is an OAuth-related error
        String errorMessage = ex.getMessage();
        if (errorMessage != null && errorMessage.contains("ID token")) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", errorMessage);
            response.put("hint", "Make sure your client sends the Google ID token in the 'accessToken' field. " +
                                "See TROUBLESHOOTING_409_ERROR.md for detailed instructions.");

            // Add specific guidance based on error type
            if (errorMessage.contains("required")) {
                response.put("solution", "The 'accessToken' field must contain a valid Google ID token (JWT). " +
                                       "In Android: .requestIdToken(WEB_CLIENT_ID) and send account.idToken");
            } else if (errorMessage.contains("verification failed")) {
                response.put("solution", "ID token verification failed. Check that:\n" +
                                       "1. Backend has correct GOOGLE_CLIENT_ID and GOOGLE_ANDROID_CLIENT_ID\n" +
                                       "2. Android client uses .requestIdToken(WEB_CLIENT_ID)\n" +
                                       "3. Token hasn't expired");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // For other IllegalArgumentExceptions, return as conflict (e.g., "Email already exists")
        if (errorMessage != null &&
            (errorMessage.contains("already") || errorMessage.contains("exists") ||
             errorMessage.contains("in use"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", errorMessage));
        }

        // Default to BAD_REQUEST for other validation errors
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", errorMessage != null ? errorMessage : "Invalid request"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDB(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Database constraint violation"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
    }
}

