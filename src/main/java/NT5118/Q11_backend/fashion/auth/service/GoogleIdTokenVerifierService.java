package NT5118.Q11_backend.fashion.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Service để xác thực Google ID tokens nhận từ mobile clients
 *
 * Service này verify ID token với Google để đảm bảo:
 * - Token được Google cấp (không phải fake)
 * - Token chưa hết hạn
 * - Token được tạo cho đúng Client ID của app
 */
@Service
public class GoogleIdTokenVerifierService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleIdTokenVerifierService(
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String webClientId,
            @Value("${app.oauth2.google.android-client-id}") String androidClientId) {

        // Accept both Web Client ID and Android Client ID
        // This allows tokens from both web and Android clients
        java.util.List<String> acceptedAudiences = new java.util.ArrayList<>();

        // Add web client ID if configured
        if (webClientId != null && !webClientId.trim().isEmpty()) {
            acceptedAudiences.add(webClientId);
        }

        // Add Android client ID if configured
        if (androidClientId != null && !androidClientId.trim().isEmpty()) {
            acceptedAudiences.add(androidClientId);
        }

        if (acceptedAudiences.isEmpty()) {
            throw new IllegalStateException("No Google OAuth2 Client IDs configured. Please set GOOGLE_CLIENT_ID or GOOGLE_ANDROID_CLIENT_ID");
        }

        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(acceptedAudiences)
                .build();
    }

    /**
     * Verify Google ID token và trả về payload đã được xác thực
     *
     * @param idTokenString ID token string từ Google Sign-In SDK
     * @return GoogleIdToken.Payload chứa thông tin user đã được verify
     * @throws IllegalArgumentException nếu token không hợp lệ
     */
    public GoogleIdToken.Payload verify(String idTokenString) {
        if (idTokenString == null || idTokenString.trim().isEmpty()) {
            throw new IllegalArgumentException("ID token không được null hoặc rỗng");
        }

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("ID token không hợp lệ - xác thực thất bại");
            }
            return idToken.getPayload();
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Xác thực ID token thất bại do lỗi bảo mật: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Xác thực ID token thất bại do lỗi I/O: " + e.getMessage(), e);
        }
    }

    /**
     * Verify Google ID token và lấy email đã được xác thực
     *
     * @param idTokenString ID token string
     * @return Email address đã được verify
     */
    public String verifyAndGetEmail(String idTokenString) {
        GoogleIdToken.Payload payload = verify(idTokenString);
        String email = payload.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy email trong ID token");
        }
        return email;
    }

    /**
     * Verify Google ID token và lấy toàn bộ thông tin user đã được xác thực
     *
     * @param idTokenString ID token string
     * @return VerifiedUserInfo chứa email, name, picture, và subject (Google ID) đã được verify
     */
    public VerifiedUserInfo verifyAndGetUserInfo(String idTokenString) {
        GoogleIdToken.Payload payload = verify(idTokenString);

        String email = payload.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy email trong ID token");
        }

        return new VerifiedUserInfo(
                payload.getSubject(), // Google user ID
                email,
                (String) payload.get("name"),
                (String) payload.get("picture")
        );
    }

    /**
     * Data class cho thông tin user đã được verify từ Google
     */
    public static class VerifiedUserInfo {
        private final String id;
        private final String email;
        private final String name;
        private final String picture;

        public VerifiedUserInfo(String id, String email, String name, String picture) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.picture = picture;
        }

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getPicture() {
            return picture;
        }
    }
}

