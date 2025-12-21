# Product API Documentation

## Overview
API endpoints for managing products in the Fashion Backend application. Supports CRUD operations, search, and filtering capabilities.

## Database Collection
- **Collection Name**: `products`
- **Database**: `fashion_mongodb`

## Product Model Structure
```bash
{
  "id": "string",            // MongoDB generated ID
  "name": "string",          // Product name
  "price": "double",         // Product price (> 0)
  "thumbnail": "string",     // URL to product image
  "gender": "string",        // Target gender (Male/Female/Unisex)
  "description": "string",   // Product description (max 2000 chars)
  "color": "string",         // Product color
  "size": "string",          // Product size
  "createdAt": "datetime",   // Creation timestamp
  "updatedAt": "datetime"    // Last update timestamp
}
```

## API Endpoints

### 1. Create a New Product
**POST** `/api/products`

Creates a new product in the catalog.

**Request Body:**
```json
{
  "name": "Classic T-Shirt",
  "price": 29.99,
  "thumbnail": "https://example.com/images/tshirt.jpg",
  "gender": "Male",
  "description": "High quality cotton t-shirt",
  "color": "White",
  "size": "L"
}
```

**Validation Rules:**
- `name`: Required, 1-200 chars
- `price`: Required, > 0.0
- `thumbnail`: Required URL string
- `gender`: Required, must be "Male", "Female", or "Unisex"
- `description`: Required, max 2000 chars
- `color`: Required
- `size`: Required

**Response (201 Created):**
```json
{
  "message": "Product created successfully",
  "product": {
    "id": "prod123",
    "name": "Classic T-Shirt",
    "price": 29.99,
    "thumbnail": "https://example.com/images/tshirt.jpg",
    "gender": "Male",
    "description": "High quality cotton t-shirt",
    "color": "White",
    "size": "L",
    "createdAt": "2025-12-16T10:00:00",
    "updatedAt": "2025-12-16T10:00:00"
  }
}
```

**Example (cURL):**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Classic T-Shirt",
    "price": 29.99,
    "thumbnail": "https://example.com/images/tshirt.jpg",
    "gender": "Male",
    "description": "High quality cotton t-shirt",
    "color": "White",
    "size": "L"
  }'
```

---

### 2. Get All Products
**GET** `/api/products`

Retrieves all products in the catalog.

**Response (200 OK):**
```bash
[
  {
    "id": "prod123",
    "name": "Classic T-Shirt",
    "price": 29.99,
    // ... other fields
  },
  {
    "id": "prod124",
    "name": "Denim Jeans",
    "price": 59.99,
    // ... other fields
  }
]
```

---

### 3. Get Product by ID
**GET** `/api/products/{id}`

Retrieves details of a specific product.

**Path Parameters:**
- `id`: The product ID

**Response (200 OK):**
```json
{
  "id": "prod123",
  "name": "Classic T-Shirt",
  "price": 29.99,
  "thumbnail": "https://example.com/images/tshirt.jpg",
  "gender": "Male",
  "description": "High quality cotton t-shirt",
  "color": "White",
  "size": "L",
  "createdAt": "2025-12-16T10:00:00",
  "updatedAt": "2025-12-16T10:00:00"
}
```

---

### 4. Search Products
**GET** `/api/products/search?keyword={keyword}`

Search for products by name or description.

**Query Parameters:**
- `keyword`: Search term (optional)

**Response (200 OK):**
List of matching products.

---

### 5. Filter by Gender
**GET** `/api/products/filter/gender?value={gender}`

Filter products by gender category.

**Query Parameters:**
- `value`: "Male", "Female", or "Unisex"

**Response (200 OK):**
List of matching products.

---

### 6. Filter by Price Range
**GET** `/api/products/filter/price?min={min}&max={max}`

Filter products within a specific price range.

**Query Parameters:**
- `min`: Minimum price (optional)
- `max`: Maximum price (optional)

**Response (200 OK):**
List of matching products.

---

### 7. Update Product
**PUT** `/api/products/{id}`

Updates an existing product.

**Path Parameters:**
- `id`: The product ID

**Request Body:**
Same as Create Product (all fields required).

**Response (200 OK):**
```bash
{
  "message": "Product updated successfully",
  "product": {
    // ... updated product details
  }
}
```

---

### 8. Delete Product
**DELETE** `/api/products/{id}`

Removes a product from the catalog.

**Path Parameters:**
- `id`: The product ID

**Response (200 OK):**
```json
{
  "message": "Product deleted successfully"
}
```

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "price",
      "message": "Price must be greater than 0"
    }
  ]
}
```

### Not Found (404)
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found"
}
```
