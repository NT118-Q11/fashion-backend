# 🔧 Cấu Hình Google Cloud Console

## ⚠️ QUAN TRỌNG - Cấu Hình Redirect URIs

Để OAuth hoạt động, bạn cần đảm bảo redirect URIs được cấu hình **CHÍNH XÁC** trong Google Cloud Console.

## Các Bước Cấu Hình

### 1. Mở Google Cloud Console
1. Truy cập: https://console.cloud.google.com/
2. Chọn project của bạn
3. Vào **APIs & Services** > **Credentials**

### 2. Tìm OAuth 2.0 Client ID
- Tìm client ID: `948358017890-ud7dt5ca4fpshmqfv6gupd90boas31bv.apps.googleusercontent.com`
- Click vào tên client để edit

### 3. Thêm Authorized Redirect URIs

**QUAN TRỌNG**: Phải có **2 redirect URIs** này:

```
http://localhost:8080/login/oauth2/code/google
http://localhost:8080/api/auth/oauth2/callback/google
```

### 4. Thêm Authorized JavaScript Origins

```
http://localhost:8080
```

### 5. Lưu Thay Đổi
- Click **Save** ở cuối trang
- Đợi vài giây để Google cập nhật

## Kiểm Tra OAuth Flow

### Cách 1: Server-side Flow (Redirect)
1. Mở trình duyệt
2. Truy cập: `http://localhost:8080/api/auth/oauth2/google`
3. Bạn sẽ được redirect tới Google login
4. Sau khi đăng nhập, Google redirect về: `http://localhost:8080/api/auth/oauth2/callback/google`
5. Backend xử lý và redirect tới frontend: `http://localhost:3000/oauth2/success?userId=...`

### Cách 2: Client-side Flow (POST Token)
Sử dụng Google SDK từ frontend, sau đó POST token lên backend.

## Các Lỗi Thường Gặp

### ❌ Error 401: invalid_client
**Nguyên nhân**: Client ID hoặc Client Secret sai
**Giải pháp**: 
- Kiểm tra lại file `.env`
- Restart ứng dụng Spring Boot

### ❌ Error 400: redirect_uri_mismatch
**Nguyên nhân**: Redirect URI không khớp với Google Cloud Console
**Giải pháp**:
1. Copy chính xác URI từ error message
2. Thêm URI đó vào **Authorized redirect URIs** trong Google Console
3. Đợi vài giây rồi thử lại

### ❌ Error 403: access_denied
**Nguyên nhân**: User từ ch��i quyền hoặc OAuth Consent Screen chưa setup
**Giải pháp**:
1. Vào **OAuth consent screen** trong Google Console
2. Cấu hình thông tin ứng dụng
3. Nếu dùng **Internal**, thêm test users
4. Nếu dùng **External**, publish app hoặc thêm test users

## Test URLs

### Local Development
- **Start OAuth**: http://localhost:8080/api/auth/oauth2/google
- **Callback** (auto): http://localhost:8080/api/auth/oauth2/callback/google
- **Frontend redirect**: http://localhost:3000/oauth2/success

### Production (khi deploy)
Nhớ thêm domain production vào:
- Authorized JavaScript origins: `https://yourdomain.com`
- Authorized redirect URIs: `https://yourdomain.com/api/auth/oauth2/callback/google`

## Restart Ứng Dụng

Sau khi cấu hình xong:

### Option 1: IntelliJ IDEA
1. Click nút **Stop** (hình vuông đỏ)
2. Click nút **Run** (hình tam giác xanh)

### Option 2: Gradle Command
```powershell
cd C:\Users\tung\IdeaProjects\fashion-backend
.\gradlew bootRun
```

### Option 3: Build và Run JAR
```powershell
.\gradlew clean build -x test
java -jar build/libs/fashion-0.0.1-SNAPSHOT.jar
```

## Logs Để Debug

Khi ứng dụng khởi động, check logs:
- ✅ **"Started FashionApplication"** - App đã start
- ✅ Không có warning về .env - File .env đã load thành công
- ❌ Nếu có lỗi OAuth - Check lại credentials

## Frontend Integration (Optional)

Nếu bạn có frontend ở `http://localhost:3000`, thêm route:
- `/oauth2/success` - Nhận userId và email từ query params
- Lưu vào localStorage/sessionStorage
- Redirect về dashboard

---

**Sau khi cấu hình xong Google Console, restart lại ứng dụng và test bằng cách mở:**
```
http://localhost:8080/api/auth/oauth2/google
```

Chúc bạn thành công! 🚀

