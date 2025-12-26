# Password Reset API Guide

## Tổng quan

Tính năng quên mật khẩu cho phép người dùng reset mật khẩu thông qua OTP (One-Time Password) gửi qua email.

## Flow hoạt động

1. **User gửi yêu cầu quên mật khẩu** → Server tạo OTP 6 chữ số và gửi về email
2. **User xác minh OTP** (optional) → Server kiểm tra OTP hợp lệ
3. **User reset mật khẩu với OTP** → Server đổi mật khẩu nếu OTP đúng

## Cấu hình Gmail SMTP

Trước khi sử dụng tính năng này, bạn cần cấu hình Gmail SMTP trong `application.properties` hoặc qua biến môi trường:

### Cách 1: Sử dụng biến môi trường (Recommended)

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### Cách 2: Cấu hình trực tiếp trong application.properties

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### ⚠️ Quan trọng: Tạo App Password cho Gmail

Google không cho phép sử dụng mật khẩu thường cho ứng dụng. Bạn cần tạo **App Password**:

1. Truy cập [Google Account](https://myaccount.google.com/)
2. Vào **Security** → **2-Step Verification** (phải bật trước)
3. Cuộn xuống → **App passwords**
4. Chọn app: "Mail", device: "Other (Custom name)" → "Fashion Backend"
5. Click **Generate** → Copy mật khẩu 16 ký tự
6. Sử dụng mật khẩu này cho `MAIL_PASSWORD`

## API Endpoints

### 1. Yêu cầu reset mật khẩu (Gửi OTP)

**POST** `/api/auth/password/forgot`

**Request Body:**
```json
{
    "email": "user@example.com"
}
```

**Response (200 OK):**
```json
{
    "message": "If an account exists with this email, you will receive an OTP shortly.",
    "email": "user@example.com"
}
```

**Lưu ý:** Response luôn trả về thành công để tránh tiết lộ email nào tồn tại trong hệ thống (security best practice).

---

### 2. Xác minh OTP (Optional)

**POST** `/api/auth/password/verify-otp`

**Request Body:**
```json
{
    "email": "user@example.com",
    "otp": "123456"
}
```

**Response (200 OK):**
```json
{
    "message": "OTP is valid",
    "valid": true
}
```

**Response (400 Bad Request):**
```json
{
    "error": "Invalid or expired OTP",
    "valid": false
}
```

---

### 3. Reset mật khẩu

**POST** `/api/auth/password/reset`

**Request Body:**
```json
{
    "email": "user@example.com",
    "otp": "123456",
    "newPassword": "newSecurePassword123"
}
```

**Response (200 OK):**
```json
{
    "message": "Password reset successfully. You can now login with your new password."
}
```

**Response (400 Bad Request):**
```json
{
    "error": "Failed to reset password. Invalid or expired OTP."
}
```

## Validation Rules

| Field | Rules |
|-------|-------|
| email | Required, valid email format |
| otp | Required, exactly 6 digits |
| newPassword | Required, minimum 6 characters |

## OTP Settings

- **Độ dài OTP:** 6 chữ số
- **Thời gian hết hạn:** 10 phút
- **Lưu ý:** Mỗi lần yêu cầu OTP mới, các OTP cũ sẽ bị vô hiệu hóa

## Ví dụ sử dụng với cURL

### Bước 1: Yêu cầu OTP
```bash
curl -X POST http://localhost:8080/api/auth/password/forgot \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com"}'
```

### Bước 2: Xác minh OTP (optional)
```bash
curl -X POST http://localhost:8080/api/auth/password/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "otp": "123456"}'
```

### Bước 3: Reset mật khẩu
```bash
curl -X POST http://localhost:8080/api/auth/password/reset \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "otp": "123456", "newPassword": "newPassword123"}'
```

## Android Integration Example

```kotlin
// Data classes
data class ForgotPasswordRequest(val email: String)
data class VerifyOtpRequest(val email: String, val otp: String)
data class ResetPasswordRequest(val email: String, val otp: String, val newPassword: String)

// Retrofit API interface
interface AuthApi {
    @POST("api/auth/password/forgot")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Map<String, Any>>
    
    @POST("api/auth/password/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<Map<String, Any>>
    
    @POST("api/auth/password/reset")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Map<String, Any>>
}

// Usage in ViewModel
class ForgotPasswordViewModel : ViewModel() {
    fun requestOtp(email: String) {
        viewModelScope.launch {
            val response = authApi.forgotPassword(ForgotPasswordRequest(email))
            if (response.isSuccessful) {
                // Navigate to OTP input screen
            }
        }
    }
    
    fun verifyAndResetPassword(email: String, otp: String, newPassword: String) {
        viewModelScope.launch {
            val response = authApi.resetPassword(
                ResetPasswordRequest(email, otp, newPassword)
            )
            if (response.isSuccessful) {
                // Navigate to login screen
            }
        }
    }
}
```

## Security Considerations

1. **Rate Limiting:** Nên implement rate limiting để tránh brute force OTP
2. **Email Enumeration:** API không tiết lộ email có tồn tại hay không
3. **OTP Expiry:** OTP tự động hết hạn sau 10 phút
4. **Single Use:** Mỗi OTP chỉ sử dụng được 1 lần
5. **Previous OTP Invalidation:** Khi yêu cầu OTP mới, tất cả OTP cũ bị vô hiệu hóa

