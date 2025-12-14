# Quick Start Script for Fashion Backend
# Kiểm tra và restart ứng dụng với .env file

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  Fashion Backend - Quick Start" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# Check if .env file exists
if (Test-Path ".env") {
    Write-Host "✓ File .env tìm thấy" -ForegroundColor Green

    # Display environment variables (masked)
    Write-Host "`nBiến môi trường trong .env:" -ForegroundColor Yellow
    Get-Content .env | ForEach-Object {
        if ($_ -match "^([^=]+)=(.*)$") {
            $key = $matches[1]
            $value = $matches[2]

            # Mask sensitive values
            if ($value.Length -gt 20) {
                $masked = $value.Substring(0, 15) + "..." + $value.Substring($value.Length - 5)
            } else {
                $masked = $value
            }

            Write-Host "  $key = $masked" -ForegroundColor Gray
        }
    }
} else {
    Write-Host "✗ File .env không tồn tại!" -ForegroundColor Red
    Write-Host "  Tạo file .env từ .env.example" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Check if MongoDB is running (optional)
Write-Host "Kiểm tra MongoDB..." -ForegroundColor Yellow
$mongoRunning = netstat -an | Select-String "27017" | Select-String "LISTENING"
if ($mongoRunning) {
    Write-Host "✓ MongoDB đang chạy trên port 27017" -ForegroundColor Green
} else {
    Write-Host "✗ MongoDB không chạy - Bạn có thể cần start Docker Compose" -ForegroundColor Yellow
    Write-Host "  Run: docker-compose up -d" -ForegroundColor Gray
}

Write-Host ""

# Build the project
Write-Host "Building project..." -ForegroundColor Yellow
.\gradlew clean build -x test

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Build thành công!" -ForegroundColor Green
} else {
    Write-Host "✗ Build thất bại!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  Khởi động ứng dụng..." -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Test OAuth tại: http://localhost:8080/api/auth/oauth2/google" -ForegroundColor Green
Write-Host "Nhấn Ctrl+C để dừng server" -ForegroundColor Gray
Write-Host ""

# Run the application
.\gradlew bootRun

