# Hướng dẫn sử dụng Google OAuth2 Flow

## Tổng quan
Backend hiện hỗ trợ hai cách đăng nhập/đăng ký bằng Google:

### 1. **Server-side Redirect Flow** (Mới thêm - sử dụng GET)
Flow này redirect người dùng tới trang đăng nhập Google, sau đó Google redirect về backend với authorization code.

### 2. **Client-side Token Flow** (POST endpoints đã có)
Client (mobile/web) tự lấy token từ Google SDK rồi gửi lên backend.

---

## Server-side Redirect Flow (GET endpoints)

### Bước 1: Redirect người dùng tới Google
**Endpoint:** `GET /api/auth/oauth2/google`

**Cách sử dụng:**
- Từ frontend, redirect người dùng tới: `http://localhost:8080/api/auth/oauth2/google`
- Hoặc đặt link/button: `<a href="http://localhost:8080/api/auth/oauth2/google">Login with Google</a>`

**Flow:**
1. Backend sẽ tạo URL authorization của Google và redirect (302)
2. Người dùng thấy trang đăng nhập/consent của Google
3. Sau khi đồng ý, Google redirect về callback endpoint của backend

### Bước 2: Callback từ Google (tự động)
**Endpoint:** `GET /api/auth/oauth2/callback/google`

**Flow tự động:**
1. Google gửi `?code=...&state=...` về endpoint này
2. Backend đổi `code` → `access_token` bằng POST tới Google token endpoint
3. Backend dùng `access_token` lấy user info từ Google
4. Backend tạo/login user vào hệ thống
5. Backend redirect về frontend: `http://localhost:3000/oauth2/success?userId=123&email=user@gmail.com`

**Tùy chỉnh redirect URL:**
- Trong `AuthController.handleGoogleCallback()`, thay đổi URL redirect cuối cùng:
  ```bash
  response.sendRedirect("http://localhost:3000/oauth2/success?userId=" + user.getId());
  ```
- Hoặc truyền JWT token:
  ```bash
  response.sendRedirect("http://localhost:3000/oauth2/success?token=" + jwtToken);
  ```

---

## Client-side Token Flow (POST endpoints)

### Đăng ký với Google
**Endpoint:** `POST /api/auth/register-gmail`

**Request Body:**
```json
{
  "id": "google-user-id",
  "email": "user@gmail.com",
  "name": "User Name",
  "picture": "https://..."
}
```

### Đăng nhập với Google
**Endpoint:** `POST /api/auth/login-gmail`

**Request Body:** (giống như register-gmail)

---

## Cấu hình

### 1. Google Console

**Cho Web Client (Server-side Flow):**
1. Tạo OAuth 2.0 Client ID với type: **Web application**
2. Thêm Authorized redirect URI:
```
http://localhost:8080/api/auth/oauth2/callback/google
```

**Cho Android Client (Mobile App):**
1. Tạo OAuth 2.0 Client ID với type: **Android**
2. Package name: `com.yourapp.package`
3. SHA-1 certificate fingerprint: `81:30:0C:1B:2A:95:84:68:AE:4F:1D:C0:EB:21:E3:69:51:AD:63:68`
4. Lưu lại Android Client ID

### 2. application.properties
```properties
# Google OAuth2 credentials - Web Client (for server-side flow)
spring.security.oauth2.client.registration.google.client-id=YOUR_WEB_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET

# Android Client ID (for mobile app ID token verification)
app.oauth2.google.android-client-id=YOUR_ANDROID_CLIENT_ID

# Custom redirect URI
app.oauth2.google.redirect-uri=http://localhost:8080/api/auth/oauth2/callback/google
```

**⚠️ QUAN TRỌNG:**
- **Web Client ID**: Dùng cho server-side OAuth flow (GET endpoints)
- **Android Client ID**: Dùng để verify ID tokens từ Android app (POST endpoints)
- Backend sẽ accept tokens từ cả 2 Client IDs

**Production:** Thay `http://localhost:8080` bằng domain thực tế của bạn.

### 3. Frontend redirect URL
Trong `AuthController.handleGoogleCallback()`, cập nhật URL frontend:
```bash
response.sendRedirect("https://yourdomain.com/oauth2/success?userId=" + user.getId());
```

---

## Kiểm tra Flow

### Test Server-side Flow:
1. Mở trình duyệt, truy cập: `http://localhost:8080/api/auth/oauth2/google`
2. Đăng nhập bằng tài khoản Google
3. Sau khi consent, bạn sẽ được redirect về: `http://localhost:3000/oauth2/success?userId=...&email=...`

### Test Client-side Flow:
```bash
curl -X POST http://localhost:8080/api/auth/login-gmail \
  -H "Content-Type: application/json" \
  -d '{
    "id": "123456",
    "email": "test@gmail.com",
    "name": "Test User",
    "picture": "https://..."
  }'
```

---

## Bảo mật - TODO

### 1. State Parameter (CSRF Protection)
Hiện tại `state` được tạo nhưng chưa verify. Cần:
- Lưu `state` vào session/Redis khi redirect
- Verify `state` trong callback

**Ví dụ cải thiện:**
```bash
// Trong redirectToGoogle():
String state = UUID.randomUUID().toString();
session.setAttribute("oauth2_state", state);

// Trong handleGoogleCallback():
String savedState = (String) session.getAttribute("oauth2_state");
if (!state.equals(savedState)) {
    throw new IllegalStateException("Invalid state parameter");
}
```

### 2. JWT Token Generation
Thay vì trả `userId` trong URL, nên trả JWT:
```bash
String jwtToken = jwtService.generateToken(user);
response.sendRedirect("http://localhost:3000/oauth2/success?token=" + jwtToken);
```

### 3. HTTPS
Production phải dùng HTTPS cho tất cả OAuth2 endpoints.

---

## Code Implementation Details

### GoogleOAuth2Service
- **getAuthorizationUrl(state)**: Tạo URL redirect tới Google consent screen
- **exchangeCodeForToken(code)**: Đổi authorization code → access_token (sử dụng RestTemplate POST)
- **getUserInfo(accessToken)**: Lấy thông tin user từ Google API

### AuthController
- **GET /api/auth/oauth2/google**: Endpoint khởi tạo redirect
- **GET /api/auth/oauth2/callback/google**: Endpoint callback nhận code và xử lý

### SecurityConfig
Đã cập nhật để:
- Cho phép public access: `/api/auth/oauth2/**`
- Disable CSRF cho OAuth2 endpoints

---

## Tích hợp với Frontend

### React/Vue/Angular Example:
```javascript
// Button redirect tới Google login
<button onClick={() => window.location.href = 'http://localhost:8080/api/auth/oauth2/google'}>
  Login with Google
</button>

// Xử lý callback tại /oauth2/success
// useEffect hoặc mounted:
const urlParams = new URLSearchParams(window.location.search);
const userId = urlParams.get('userId');
const email = urlParams.get('email');
// Hoặc lấy JWT token
const token = urlParams.get('token');
if (token) {
  localStorage.setItem('authToken', token);
  // Redirect to dashboard
}
```

### Android Example (AppRoute.kt):
Android sử dụng **Client-side Flow** với ID Token:

**Bước 1: Cấu hình Google Sign-In trong Android**
- Thêm SHA-1 certificate fingerprint vào Google Console
- Tạo Android OAuth 2.0 Client ID riêng
- Cấu hình `google-services.json`

**Bước 2: Lấy ID Token từ Google Sign-In**
```kotlin
val account = GoogleSignIn.getLastSignedInAccount(context)
val idToken = account?.idToken // Đây là ID token cần gửi lên backend
```

**Bước 3: Gửi ID Token lên Backend**
```kotlin
val request = GoogleOAuth2UserInfo(
    id = account.id,
    email = account.email,
    name = account.displayName,
    picture = account.photoUrl?.toString(),
    accessToken = idToken  // ⚠️ Quan trọng: gửi ID token ở field accessToken
)

// POST to /api/auth/login-gmail
apiService.loginWithGoogle(request)
```

**⚠️ LƯU Ý QUAN TRỌNG:**
- Backend cần **Android Client ID** để verify ID token từ Android app
- Android Client ID khác với Web Client ID
- Phải cấu hình trong `application.properties`: `app.oauth2.google.android-client-id`

---

## Troubleshooting

### Lỗi: "redirect_uri_mismatch"
- Kiểm tra Google Console → Credentials → Authorized redirect URIs
- Phải khớp chính xác: `http://localhost:8080/api/auth/oauth2/callback/google`

### Lỗi: CSRF protection
- Thêm `/api/auth/oauth2/**` vào CSRF ignore list (đã làm)

### Lỗi: CORS
- Frontend origin phải được thêm vào SecurityConfig corsConfigurationSource()

---

## Next Steps

- [ ] Implement JWT token generation
- [ ] Add state parameter verification (CSRF)
- [ ] Store state in Redis/session
- [ ] Add proper logging instead of printStackTrace()
- [ ] Add rate limiting for OAuth2 endpoints
- [ ] Add user session management
- [ ] Support refresh token flow

