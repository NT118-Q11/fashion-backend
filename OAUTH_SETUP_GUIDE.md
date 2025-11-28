# Hướng dẫn thiết lập đăng nhập Gmail cho Fashion Backend

## 1. Thiết lập Google OAuth2 Credentials

### Bước 1: Tạo Google Cloud Project
1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo một project mới hoặc chọn project hiện có
3. Kích hoạt Google+ API và Google OAuth2 API

### Bước 2: Tạo OAuth2 Credentials
1. Vào **APIs & Services** > **Credentials**
2. Click **Create Credentials** > **OAuth 2.0 Client IDs**
3. Chọn **Web application**
4. Cấu hình:
   - **Name**: Fashion Backend OAuth
   - **Authorized JavaScript origins**: 
     - `http://localhost:8080`
     - `http://localhost:3000` (nếu có frontend)
   - **Authorized redirect URIs**:
     - `http://localhost:8080/login/oauth2/code/google`

### Bước 3: Cập nhật application.properties
Thay thế YOUR_GOOGLE_CLIENT_ID và YOUR_GOOGLE_CLIENT_SECRET bằng giá trị thực từ Google:

```properties
spring.security.oauth2.client.registration.google.client-id=your-actual-client-id
spring.security.oauth2.client.registration.google.client-secret=your-actual-client-secret
```

## 2. API Endpoints

### Đăng ký thông thường
**POST** `/api/auth/register`
```json
{
  "name": "Nguyen Van A",
  "email": "user@example.com",
  "password": "password123"
}
```

### Đăng nhập thông thường
**POST** `/api/auth/login`
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

### Đăng nhập bằng Gmail
**GET** `/oauth2/authorization/google`
- Redirect người dùng đến trang đăng nhập Google
- Sau khi thành công, sẽ trả về JWT token

## 3. Frontend Integration Example (JavaScript)

### Đăng ký/Đăng nhập thông thường:
```javascript
// Đăng ký
const register = async (userData) => {
  const response = await fetch('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData)
  });
  return await response.json();
};

// Đăng nhập
const login = async (credentials) => {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(credentials)
  });
  return await response.json();
};
```

### Đăng nhập bằng Gmail:
```javascript
const loginWithGoogle = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google';
};
```

## 4. Sử dụng JWT Token

Sau khi đăng nhập thành công, bạn sẽ nhận được JWT token. Sử dụng token này trong header của các request tiếp theo:

```javascript
const apiCall = async (url, options = {}) => {
  const token = localStorage.getItem('jwt_token');
  
  const response = await fetch(url, {
    ...options,
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      ...options.headers
    }
  });
  
  return await response.json();
};
```

## 5. Chạy ứng dụng

```bash
# Build project
./gradlew build

# Chạy ứng dụng
./gradlew bootRun
```

## 6. Test các endpoint

Bạn có thể test bằng Postman hoặc curl:

```bash
# Test đăng ký
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123"}'

# Test đăng nhập
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

## 7. Troubleshooting

### Lỗi thường gặp:
1. **MongoDB connection**: Đảm bảo MongoDB đang chạy trên port 27017
2. **Google OAuth error**: Kiểm tra client-id và client-secret
3. **CORS error**: Đảm bảo frontend URL được thêm vào authorized origins

### Logs để debug:
- Check console log khi start application
- MongoDB connection status
- OAuth2 authentication flow logs

## 8. Security Notes

1. **JWT Secret**: Thay đổi JWT secret trong production
2. **HTTPS**: Sử dụng HTTPS trong production
3. **Database**: Sử dụng MongoDB Atlas hoặc secure MongoDB instance
4. **Environment Variables**: Không commit sensitive data vào git

## 9. Mở rộng

Bạn có thể dễ dàng thêm:
- Email verification
- Password reset
- Refresh tokens
- Role-based authorization
- Social login khác (Facebook, GitHub, etc.)
