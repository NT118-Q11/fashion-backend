# Cart API Documentation

## Overview
API endpoints for managing user shopping carts. Allows adding items, updating quantities, removing items, and clearing the cart.

## Database Collection
- **Collection Name**: `carts`
- **Database**: `fashion_mongodb`

## Cart Model Structure
```json
{
  "id": "string",
  "userId": "string",
  "items": [
    {
      "productId": "string",
      "productName": "string",  // Embedded for performance
      "price": "double",
      "quantity": "integer",
      "subtotal": "double"
    }
  ],
  "totalPrice": "double",
  "totalItems": "integer",
  "updatedAt": "datetime"
}
```

## API Endpoints

### 1. Add Item to Cart
**POST** `/api/cart/items`

Adds a product to the user's cart. If the item exists, increments quantity.

**Request Body:**
```json
{
  "userId": "user123",
  "productId": "prod456",
  "quantity": 1
}
```

**Validation:**
- `userId`: Required
- `productId`: Required
- `quantity`: ≥ 1

**Response (201 Created):**
```json
{
  "message": "Product added to cart successfully",
  "cart": {
    "id": "cart789",
    "userId": "user123",
    "items": [...],
    "totalPrice": 100.0,
    "totalItems": 3,
    "updatedAt": "..."
  }
}
```

---

### 2. Get User's Cart
**GET** `/api/cart?userId={userId}`

Retrieves the current state of a user's cart.

**Query Parameters:**
- `userId`: The ID of the user

**Response (200 OK):**
Returns the `CartResponse` object.

---

### 3. Update Cart Item Quantity
**PUT** `/api/cart/items/{productId}?userId={userId}`

Updates quantity of a specific product in the cart.

**Path Parameters:**
- `productId`: The ID of the product in the cart

**Query Parameters:**
- `userId`: The owner of the cart

**Request Body:**
```json
{
  "quantity": 3
}
```

**Response (200 OK):**
Returns updated cart structure.

---

### 4. Remove Item from Cart
**DELETE** `/api/cart/items/{productId}?userId={userId}`

Removes a specific product from the cart completely.

**Path Parameters:**
- `productId`: The ID of the product to remove

**Query Parameters:**
- `userId`: The owner of the cart

**Response (200 OK):**
```json
{
  "message": "Item removed from cart successfully",
  "cart": { ... }
}
```

---

### 5. Clear Cart
**DELETE** `/api/cart?userId={userId}`

Removes all items from the user's cart.

**Query Parameters:**
- `userId`: The user ID

**Response (200 OK):**
```json
{
  "message": "Cart cleared successfully",
  "cart": {
    "items": [],
    "totalPrice": 0,
    "totalItems": 0,
    ...
  }
}
```

## Error Responses

### Item Not Found
If you try to update/remove an item that isn't in the cart, you might receive a 404.

### Product Not Found
If adding a product ID that doesn't exist in the product catalog.
