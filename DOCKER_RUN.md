# Docker Run Guide - Chạy toàn bộ project với Docker

## 🎯 Mục đích

Chạy toàn bộ project (MongoDB + Spring Boot) bằng Docker Compose, **không cần cài Java** trên máy!

## 🚀 Quick Start

### **1. Chạy tất cả services (MongoDB + Backend)**
```bash
docker-compose up -d
```

### **2. Xem logs**
```bash
# Xem logs tất cả services
docker-compose logs -f

# Xem logs chỉ backend
docker-compose logs -f app

# Xem logs chỉ MongoDB
docker-compose logs -f mongodb
```

### **3. Kiểm tra services đang chạy**
```bash
docker-compose ps
```

### **4. Test API**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### **5. Dừng services**
```bash
# Dừng nhưng giữ containers
docker-compose stop

# Dừng và xóa containers (giữ data)
docker-compose down

# Dừng và xóa tất cả (bao gồm data)
docker-compose down -v
```

---

## 📋 Chi tiết

### **Services trong Docker Compose**

1. **mongodb**: MongoDB database
   - Port: `27017`
   - Database: `fashion-mongodb`
   - Username: `admin`
   - Password: `admin123`

2. **app**: Spring Boot application
   - Port: `8080`
   - Tự động build từ Dockerfile
   - Tự động kết nối với MongoDB

### **Build và Rebuild**

**Lần đầu tiên hoặc sau khi sửa code:**
```bash
# Build lại image
docker-compose build

# Hoặc build và chạy luôn
docker-compose up -d --build
```

**Sau khi sửa code:**
```bash
# Rebuild và restart
docker-compose up -d --build app
```

### **Xem logs real-time**
```bash
# Tất cả services
docker-compose logs -f

# Chỉ backend app
docker-compose logs -f app

# Chỉ MongoDB
docker-compose logs -f mongodb
```

---

## 🔧 Development Workflow

### **Option 1: Chạy với Docker (Khuyến nghị cho team)**
```bash
# 1. Clone project
git clone <repo>
cd fashion-backend

# 2. Chạy tất cả
docker-compose up -d

# 3. Test
curl http://localhost:8080/api/auth/register

# 4. Xem logs khi develop
docker-compose logs -f app
```

**Ưu điểm:**
- ✅ Không cần cài Java
- ✅ Môi trường giống nhau cho tất cả thành viên
- ✅ Dễ setup cho người mới
- ✅ Isolated environment

### **Option 2: Chạy local (Cho development nhanh)**
```bash
# 1. Cần Java 17+ và MongoDB
# 2. Chạy MongoDB với Docker
docker-compose up -d mongodb

# 3. Chạy Spring Boot local
./gradlew bootRun
```

**Ưu điểm:**
- ✅ Hot reload nhanh hơn
- ✅ Debug dễ hơn
- ✅ Không cần rebuild Docker image

---

## 🐛 Troubleshooting

### **1. Port đã được sử dụng**
```bash
# Kiểm tra port
lsof -i :8080
lsof -i :27017

# Sửa port trong docker-compose.yml nếu cần
ports:
  - "8081:8080"  # External:Internal
```

### **2. Application không start**
```bash
# Xem logs để tìm lỗi
docker-compose logs app

# Rebuild image
docker-compose build --no-cache app
docker-compose up -d app
```

### **3. MongoDB connection error**
```bash
# Kiểm tra MongoDB đang chạy
docker-compose ps mongodb

# Kiểm tra network
docker network ls
docker network inspect fashion-backend_fashion-network
```

### **4. Build chậm**
```bash
# Build với cache
docker-compose build

# Hoặc build không cache (chậm hơn nhưng clean)
docker-compose build --no-cache
```

---

## 📁 Cấu trúc Files

```
fashion-backend/
├── Dockerfile              # Build Spring Boot app
├── docker-compose.yml      # Orchestration
├── .dockerignore           # Files to ignore khi build
├── mongodb-data/           # MongoDB data (auto-created)
└── mongodb-config/         # MongoDB config (auto-created)
```

---

## 🔐 Environment Variables

Có thể override bằng file `.env` hoặc environment variables:

```bash
# Tạo file .env
cat > .env << EOF
SPRING_DATA_MONGODB_URI=mongodb://admin:admin123@mongodb:27017/fashion-mongodb?authSource=admin
SERVER_PORT=8080
EOF
```

Hoặc set trong `docker-compose.yml`:
```yaml
environment:
  - SPRING_DATA_MONGODB_URI=mongodb://admin:admin123@mongodb:27017/fashion-mongodb?authSource=admin
```

---

## 🎯 Best Practices

1. **Development**: Dùng Docker Compose để đảm bảo môi trường giống nhau
2. **Production**: Build image riêng và deploy lên server
3. **CI/CD**: Dùng Docker Compose để test trong pipeline
4. **Team**: Mọi người chỉ cần Docker, không cần Java

---

## 📚 Commands Cheat Sheet

```bash
# Start
docker-compose up -d

# Stop
docker-compose stop

# Restart
docker-compose restart

# Rebuild
docker-compose up -d --build

# Logs
docker-compose logs -f app

# Shell vào container
docker-compose exec app sh

# Xóa tất cả
docker-compose down -v
```

---

**Tác giả**: Fashion Backend Team  
**Cập nhật**: 2024

