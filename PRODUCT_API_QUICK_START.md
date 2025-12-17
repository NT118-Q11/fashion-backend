# Quick Start Guide - Testing Product API

## Prerequisites
- MongoDB running on `localhost:27017`
- Fashion Backend application running on `http://localhost:8080`

## Quick Test with cURL (PowerShell)

### 1. Create a Product
```powershell
$body = @{
    name = "Summer Dress"
    price = 49.99
    thumbnail = "https://example.com/dress.jpg"
    gender = "Female"
    description = "Light floral dress perfect for summer"
    color = "Blue"
    size = "M"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/products" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### 2. Get All Products
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/products" -Method GET
```

### 3. Search Products
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/products/search?keyword=Summer" -Method GET
```

### 4. Filter by Gender
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/products/filter/gender?value=Female" -Method GET
```

### 5. Filter by Price
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/products/filter/price?min=20&max=100" -Method GET
```

### 6. Update a Product (replace {id})
```powershell
$body = @{
    name = "Summer Dress 2024"
    price = 59.99
    thumbnail = "https://example.com/dress_v2.jpg"
    gender = "Female"
    description = "Updated summer collection"
    color = "Blue"
    size = "M"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/products/{id}" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $body
```

### 7. Delete a Product (replace {id})
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/products/{id}" -Method DELETE
```

## Testing with Postman

### Import Collection
1. Create collection: "Fashion Backend - Products"
2. Add the following requests:

#### POST - Create Product
- **URL**: `http://localhost:8080/api/products`
- **Body**: JSON
```json
{
  "name": "Jeans",
  "price": 39.99,
  "thumbnail": "url",
  "gender": "Male",
  "description": "Denim",
  "color": "Blue",
  "size": "32"
}
```

#### GET - Search
- **URL**: `http://localhost:8080/api/products/search`
- **Params**: `keyword` = `Jeans`

## Common Issues

### Validation Error
If you get `400 Bad Request`, check:
- Price must be > 0
- Gender must be "Male", "Female", or "Unisex" (case sensitive)
- All string fields are required and not empty

### 404 Not Found
- Ensure the ID used in PUT/DELETE exists in the database.
