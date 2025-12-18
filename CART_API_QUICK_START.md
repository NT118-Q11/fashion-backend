# Quick Start Guide - Testing Cart API

## Prerequisites
- MongoDB running
- Backend running on `localhost:8080`
- Valid `userId` and `productId`s from your database

## Quick Tests (PowerShell)

### 1. Add Item to Cart
```powershell
$body = @{
    userId = "test-user-01"
    productId = "prod-123"
    quantity = 2
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/cart/items" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### 2. View Cart
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/cart?userId=test-user-01" -Method GET
```

### 3. Update Item Quantity
Change quantity of `prod-123` to 5.
```powershell
$body = @{
    quantity = 5
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/cart/items/prod-123?userId=test-user-01" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $body
```

### 4. Remove Item
Remove `prod-123` from cart.
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/cart/items/prod-123?userId=test-user-01" -Method DELETE
```

### 5. Clear Cart
Empty the whole cart.
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/cart?userId=test-user-01" -Method DELETE
```

## Common Scenarios

### Guest Cart vs User Cart
Currently the API uses `userId` string. For guest carts, the frontend typically generates a temporary unique ID (UUID) and passes it as `userId`.

### Stock Validation
*Note: Depending on implementation, adding to cart might check stock availability.* If you get an error "Out of stock", choose a different product or check inventory.
