# Client-Side Integration Guide - Google OAuth2

## Tổng quan
Backend cung cấp 2 cách tích hợp Google OAuth2:

1. **Server-side Redirect Flow** (GET endpoints) - Dùng cho Web browsers
2. **Client-side Token Flow** (POST endpoints) - Dùng cho Mobile apps

---

## Mobile Apps (Android/iOS) - Client-side Flow

### Bước 1: Tích hợp Google Sign-In SDK

#### Android - build.gradle
```gradle
dependencies {
    implementation 'com.google.android.gms:play-services-auth:20.7.0'
}
```

#### iOS - Podfile
```ruby
pod 'GoogleSignIn'
```

### Bước 2: Lấy ID Token từ Google

#### Android Example
```bash
// Activity/Fragment
private fun signInWithGoogle() {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.server_client_id)) // From Google Console
        .requestEmail()
        .build()
    
    val googleSignInClient = GoogleSignIn.getClient(this, gso)
    val signInIntent = googleSignInClient.signInIntent
    startActivityForResult(signInIntent, RC_SIGN_IN)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == RC_SIGN_IN) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        handleSignInResult(task)
    }
}

private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
        val account = completedTask.getResult(ApiException::class.java)
        // Send to backend
        sendToBackend(account)
    } catch (e: ApiException) {
        Log.e(TAG, "signInResult:failed code=" + e.statusCode)
    }
}

private suspend fun sendToBackend(account: GoogleSignInAccount) {
    val userInfo = GoogleOAuth2UserInfo(
        id = account.id ?: "",
        email = account.email ?: "",
        name = account.displayName,
        picture = account.photoUrl?.toString(),
        accessToken = account.idToken // ID Token from Google
    )
    
    // Call backend API
    val response = AppRoute.auth.loginWithGoogle(userInfo)
    if (response.user != null) {
        // Login successful
        val user = response.user
        // Save session, navigate to home
    }
}
```

### Bước 3: Retrofit API Interface

#### AppRoute.kt
```bash
package com.example.fashionapp

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Data classes
data class GoogleOAuth2UserInfo(
    val id: String,
    val email: String,
    val name: String? = null,
    val picture: String? = null,
    val accessToken: String? = null // ID token from Google SDK
)

data class UserDto(
    val id: Long,
    val username: String?,
    val email: String?,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("user_address")
    val userAddress: String? = null
)

data class ApiResponse<T>(
    val message: String,
    val user: T?
)

// Retrofit API
interface AuthApi {
    @POST("/api/auth/login-gmail")
    suspend fun loginWithGoogle(@Body info: GoogleOAuth2UserInfo): ApiResponse<UserDto>
    
    @POST("/api/auth/register-gmail")
    suspend fun registerWithGoogle(@Body info: GoogleOAuth2UserInfo): ApiResponse<UserDto>
}

object AppRoute {
    private var baseUrl: String = "http://10.0.2.2:8080" // Android emulator
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val auth: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    
    fun init(newBaseUrl: String) {
        baseUrl = newBaseUrl
    }
}
```

---

## Web Apps - Server-side Redirect Flow

### HTML/JavaScript Example
```html
<!DOCTYPE html>
<html>
<body>
    <button onclick="loginWithGoogle()">Login with Google</button>
    
    <script>
        function loginWithGoogle() {
            // Redirect to backend OAuth2 endpoint
            window.location.href = 'http://localhost:8080/api/auth/oauth2/google';
        }
        
        // Handle callback at /oauth2/success
        // This page will receive ?userId=...&email=... or ?token=...
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');
        if (token) {
            localStorage.setItem('authToken', token);
            window.location.href = '/dashboard';
        }
    </script>
</body>
</html>
```

### React Example
```jsx
// LoginPage.jsx
import React from 'react';

function LoginPage() {
    const handleGoogleLogin = () => {
        window.location.href = 'http://localhost:8080/api/auth/oauth2/google';
    };
    
    return (
        <button onClick={handleGoogleLogin}>
            Login with Google
        </button>
    );
}

// OAuth2SuccessPage.jsx
import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

function OAuth2SuccessPage() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    
    useEffect(() => {
        const token = searchParams.get('token');
        const userId = searchParams.get('userId');
        const email = searchParams.get('email');
        
        if (token) {
            localStorage.setItem('authToken', token);
            navigate('/dashboard');
        } else if (userId) {
            // Handle userId case
            console.log('User ID:', userId, 'Email:', email);
            navigate('/dashboard');
        }
    }, [searchParams, navigate]);
    
    return <div>Processing login...</div>;
}
```

---

## API Endpoints Summary

### GET Endpoints (Server-side Redirect Flow)
| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/auth/oauth2/google` | GET | Redirect user to Google login |
| `/api/auth/oauth2/callback/google` | GET | Callback from Google (handled by backend) |

### POST Endpoints (Client-side Token Flow)
| Endpoint | Method | Body | Response |
|----------|--------|------|----------|
| `/api/auth/register-gmail` | POST | `GoogleOAuth2UserInfo` | `ApiResponse<UserDto>` |
| `/api/auth/login-gmail` | POST | `GoogleOAuth2UserInfo` | `ApiResponse<UserDto>` |

---

## Configuration

### 1. Google Cloud Console
**Authorized redirect URIs:**
- Web: `http://localhost:8080/api/auth/oauth2/callback/google`
- Production: `https://yourdomain.com/api/auth/oauth2/callback/google`

**OAuth 2.0 Client IDs:**
- Web application: Use for server-side flow
- Android/iOS: Use for mobile SDK

### 2. Backend - application.properties
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_WEB_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
app.oauth2.google.redirect-uri=http://localhost:8080/api/auth/oauth2/callback/google
```

### 3. Android - strings.xml
```xml
<string name="server_client_id">YOUR_WEB_CLIENT_ID.apps.googleusercontent.com</string>
```

**Note:** Android apps should use the **Web client ID** (not Android client ID) when requesting ID token for backend verification.

---

## Flow Diagrams

### Server-side Redirect Flow (Web)
```
User → Frontend (click "Login with Google")
     → Backend GET /api/auth/oauth2/google
     → Google (302 redirect to consent screen)
     → User approves
     → Google → Backend GET /api/auth/oauth2/callback/google?code=...
     → Backend exchanges code for token
     → Backend creates/logins user
     → Backend → Frontend (302 redirect with token/userId)
     → Frontend saves token & navigates to dashboard
```

### Client-side Token Flow (Mobile)
```
User → Mobile App (click "Login with Google")
     → Google Sign-In SDK
     → User approves
     → SDK returns ID token
     → App → Backend POST /api/auth/login-gmail with token
     → Backend verifies token (TODO: add verification)
     → Backend creates/logins user
     → Backend → App (JSON response with user data)
     → App saves session & navigates to home
```

---

## Security Recommendations

### For Server-side Flow:
1. ✅ HTTPS only in production
2. ✅ State parameter for CSRF protection (TODO: add verification)
3. ✅ Secure session/cookie with httpOnly, secure flags
4. ✅ Short-lived access tokens

### For Client-side Flow:
1. ⚠️ **MUST verify ID token on backend** (currently not implemented)
2. ✅ Never trust client-sent data without verification
3. ✅ Use HTTPS only
4. ✅ Implement rate limiting

### Backend TODO:
```java
// Add ID token verification in AuthService
public User registerOAuthUser(GoogleOAuth2UserInfo info) {
    // Verify ID token with Google
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();

    GoogleIdToken idToken = verifier.verify(info.getAccessToken());
    if (idToken == null) {
        throw new IllegalArgumentException("Invalid ID token");
    }
    
    GoogleIdToken.Payload payload = idToken.getPayload();
    // Use verified payload instead of client-sent info
    String email = payload.getEmail();
    // ...
}
```

---

## Testing

### Test Server-side Flow:
1. Open browser: `http://localhost:8080/api/auth/oauth2/google`
2. Login with Google account
3. Should redirect to: `http://localhost:3000/oauth2/success?userId=...`

### Test Client-side Flow:
```bash
curl -X POST http://localhost:8080/api/auth/login-gmail \
  -H "Content-Type: application/json" \
  -d '{
    "id": "123456789",
    "email": "test@gmail.com",
    "name": "Test User",
    "picture": "https://lh3.googleusercontent.com/..."
  }'
```

**Response:**
```json
{
  "message": "Login with Google successful",
  "user": {
    "id": 1,
    "username": "test",
    "email": "test@gmail.com",
    "phone_number": "0",
    "user_address": ""
  }
}
```

---

## Troubleshooting

### Mobile: "idpiframe_initialization_failed"
- Make sure you're using the **Web client ID** in your mobile app
- The web client ID should be the same one configured in backend

### Web: "redirect_uri_mismatch"
- Check Google Console authorized redirect URIs
- Must exactly match: `http://localhost:8080/api/auth/oauth2/callback/google`

### Backend: "Invalid ID token"
- Not yet implemented - need to add token verification
- See Security Recommendations section

---

## References
- [Google OAuth2 Documentation](https://developers.google.com/identity/protocols/oauth2)
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2)

