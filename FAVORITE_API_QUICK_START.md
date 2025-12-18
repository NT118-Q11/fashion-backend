# Quick Start Guide - Testing Favorite API

## Prerequisites
- Server running (`./gradlew bootRun`)
- MongoDB running

## Quick Tests (PowerShell)

### 1. Add Favorite
```powershell
$body = @{
    userId = "test-user-01"
    productId = "prod-123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/favorites" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### 2. Check Status
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/favorites/check?userId=test-user-01&productId=prod-123" -Method GET
```

### 3. List Favorites
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/favorites?userId=test-user-01" -Method GET
```

### 4. Remove Favorite
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/favorites?userId=test-user-01&productId=prod-123" -Method DELETE
```
