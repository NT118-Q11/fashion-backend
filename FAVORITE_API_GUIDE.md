# Favorite API Documentation

## Overview
API endpoints for managing user favorites (Wishlist).

## Database Collection
- **Collection Name**: `favorites`
- **Database**: `fashion_mongodb`

## Favorite Model Structure
```json
{
  "id": "string",
  "userId": "string",
  "productId": "string",
  "createdAt": "datetime"
}
```

## API Endpoints

### 1. Add to Favorites
**POST** `/api/favorites`

Adds a product to the user's favorites.

**Request Body:**
```json
{
  "userId": "user123",
  "productId": "prod456"
}
```

**Response (200 OK):**
```json
{
  "id": "fav789",
  "userId": "user123",
  "productId": "prod456",
  "createdAt": "..."
}
```

---

### 2. Get User Favorites
**GET** `/api/favorites?userId={userId}`

Retrieves all favorite items for a user.

**Response (200 OK):**
```json
[
  {
    "id": "fav789",
    "userId": "user123",
    "productId": "prod456",
    "createdAt": "...",
    "product": { ... } // Full product details if available
  }
]
```

---

### 3. Check Favorite Status
**GET** `/api/favorites/check?userId={userId}&productId={productId}`

Checks if a specific product is in user's favorites.

**Response (200 OK):**
```json
{
  "isFavorite": true
}
```

---

### 4. Remove from Favorites
**DELETE** `/api/favorites?userId={userId}&productId={productId}`

Removes a product from favorites.

**Response (200 OK):**
```json
{
  "message": "Removed from favorites"
}
```
