# Rating API Documentation

## Overview
API endpoints for managing product ratings in the Fashion Backend application. Users can create, retrieve, update, and delete ratings for products.

## Database Collection
- **Collection Name**: `ratings`
- **Database**: `fashion_mongodb`

## Rating Model Structure
```json
{
  "id": "string",           // MongoDB generated ID
  "userId": "string",       // ID of the user who created the rating
  "productId": "string",    // ID of the product being rated
  "rateStars": "integer",   // Rating value (1-5 stars)
  "comment": "string",      // User's comment/review
  "createdAt": "datetime",  // Timestamp when rating was created
  "updatedAt": "datetime"   // Timestamp when rating was last updated
}
```

## API Endpoints

### 1. Create a New Rating
**POST** `/api/ratings`

Creates a new rating for a product.

**Request Body:**
```json
{
  "userId": "user123",
  "productId": "product456",
  "rateStars": 5,
  "comment": "Excellent product! Highly recommended."
}
```

**Validation Rules:**
- `userId`: Required, must not be blank
- `productId`: Required, must not be blank
- `rateStars`: Required, must be between 1 and 5 (inclusive)
- `comment`: Optional

**Response (201 Created):**
```json
{
  "id": "rating789",
  "userId": "user123",
  "productId": "product456",
  "rateStars": 5,
  "comment": "Excellent product! Highly recommended.",
  "createdAt": "2025-12-16T10:30:00",
  "updatedAt": "2025-12-16T10:30:00"
}
```

**Example (cURL):**
```bash
curl -X POST http://localhost:8080/api/ratings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "userId": "user123",
    "productId": "product456",
    "rateStars": 5,
    "comment": "Excellent product!"
  }'
```

---

### 2. Get All Ratings
**GET** `/api/ratings`

Retrieves all ratings in the system.

**Response (200 OK):**
```json
[
  {
    "id": "rating789",
    "userId": "user123",
    "productId": "product456",
    "rateStars": 5,
    "comment": "Excellent product!",
    "createdAt": "2025-12-16T10:30:00",
    "updatedAt": "2025-12-16T10:30:00"
  },
  {
    "id": "rating790",
    "userId": "user124",
    "productId": "product457",
    "rateStars": 4,
    "comment": "Good quality",
    "createdAt": "2025-12-16T11:00:00",
    "updatedAt": "2025-12-16T11:00:00"
  }
]
```

**Example (cURL):**
```bash
curl -X GET http://localhost:8080/api/ratings \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 3. Get Rating by ID
**GET** `/api/ratings/{id}`

Retrieves a specific rating by its ID.

**Path Parameters:**
- `id`: The rating ID

**Response (200 OK):**
```json
{
  "id": "rating789",
  "userId": "user123",
  "productId": "product456",
  "rateStars": 5,
  "comment": "Excellent product!",
  "createdAt": "2025-12-16T10:30:00",
  "updatedAt": "2025-12-16T10:30:00"
}
```

**Response (404 Not Found):**
Rating not found

**Example (cURL):**
```bash
curl -X GET http://localhost:8080/api/ratings/rating789 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 4. Get Ratings by Product ID
**GET** `/api/ratings/product/{productId}`

Retrieves all ratings for a specific product.

**Path Parameters:**
- `productId`: The product ID

**Response (200 OK):**
```json
[
  {
    "id": "rating789",
    "userId": "user123",
    "productId": "product456",
    "rateStars": 5,
    "comment": "Excellent product!",
    "createdAt": "2025-12-16T10:30:00",
    "updatedAt": "2025-12-16T10:30:00"
  },
  {
    "id": "rating791",
    "userId": "user125",
    "productId": "product456",
    "rateStars": 4,
    "comment": "Very good",
    "createdAt": "2025-12-16T12:00:00",
    "updatedAt": "2025-12-16T12:00:00"
  }
]
```

**Example (cURL):**
```bash
curl -X GET http://localhost:8080/api/ratings/product/product456 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 5. Get Ratings by User ID
**GET** `/api/ratings/user/{userId}`

Retrieves all ratings created by a specific user.

**Path Parameters:**
- `userId`: The user ID

**Response (200 OK):**
```json
[
  {
    "id": "rating789",
    "userId": "user123",
    "productId": "product456",
    "rateStars": 5,
    "comment": "Excellent product!",
    "createdAt": "2025-12-16T10:30:00",
    "updatedAt": "2025-12-16T10:30:00"
  },
  {
    "id": "rating792",
    "userId": "user123",
    "productId": "product458",
    "rateStars": 3,
    "comment": "Average quality",
    "createdAt": "2025-12-16T13:00:00",
    "updatedAt": "2025-12-16T13:00:00"
  }
]
```

**Example (cURL):**
```bash
curl -X GET http://localhost:8080/api/ratings/user/user123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 6. Update a Rating
**PUT** `/api/ratings/{id}`

Updates an existing rating. Only `rateStars` and `comment` can be updated.

**Path Parameters:**
- `id`: The rating ID

**Request Body:**
```json
{
  "userId": "user123",
  "productId": "product456",
  "rateStars": 4,
  "comment": "Updated review: Still good but found some issues."
}
```

**Note:** Even though `userId` and `productId` are required in the request body for validation, they cannot be changed. The original values are preserved.

**Response (200 OK):**
```json
{
  "id": "rating789",
  "userId": "user123",
  "productId": "product456",
  "rateStars": 4,
  "comment": "Updated review: Still good but found some issues.",
  "createdAt": "2025-12-16T10:30:00",
  "updatedAt": "2025-12-16T14:00:00"
}
```

**Response (404 Not Found):**
Rating not found

**Example (cURL):**
```bash
curl -X PUT http://localhost:8080/api/ratings/rating789 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "userId": "user123",
    "productId": "product456",
    "rateStars": 4,
    "comment": "Updated review"
  }'
```

---

### 7. Delete a Rating
**DELETE** `/api/ratings/{id}`

Deletes a rating by its ID.

**Path Parameters:**
- `id`: The rating ID

**Response (200 OK):**
```json
{
  "message": "Rating deleted successfully"
}
```

**Response (404 Not Found):**
Rating not found

**Example (cURL):**
```bash
curl -X DELETE http://localhost:8080/api/ratings/rating789 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "timestamp": "2025-12-16T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "rateStars",
      "message": "Rating must be at least 1 star"
    }
  ]
}
```

### Not Found (404)
```json
{
  "timestamp": "2025-12-16T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Rating not found"
}
```

---

## Frontend Integration Examples

### JavaScript/Fetch API

```javascript
// Create a rating
async function createRating(userId, productId, stars, comment) {
  const response = await fetch('http://localhost:8080/api/ratings', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
    },
    body: JSON.stringify({
      userId: userId,
      productId: productId,
      rateStars: stars,
      comment: comment
    })
  });
  return await response.json();
}

// Get ratings for a product
async function getProductRatings(productId) {
  const response = await fetch(`http://localhost:8080/api/ratings/product/${productId}`, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
    }
  });
  return await response.json();
}

// Update a rating
async function updateRating(ratingId, userId, productId, stars, comment) {
  const response = await fetch(`http://localhost:8080/api/ratings/${ratingId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
    },
    body: JSON.stringify({
      userId: userId,
      productId: productId,
      rateStars: stars,
      comment: comment
    })
  });
  return await response.json();
}

// Delete a rating
async function deleteRating(ratingId) {
  const response = await fetch(`http://localhost:8080/api/ratings/${ratingId}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
    }
  });
  return await response.json();
}
```

### React Example Component

```jsx
import React, { useState, useEffect } from 'react';

function ProductRatings({ productId }) {
  const [ratings, setRatings] = useState([]);
  const [newRating, setNewRating] = useState({ stars: 5, comment: '' });

  useEffect(() => {
    fetchRatings();
  }, [productId]);

  const fetchRatings = async () => {
    const response = await fetch(`http://localhost:8080/api/ratings/product/${productId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
      }
    });
    const data = await response.json();
    setRatings(data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const userId = localStorage.getItem('user_id');
    
    await fetch('http://localhost:8080/api/ratings', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
      },
      body: JSON.stringify({
        userId: userId,
        productId: productId,
        rateStars: newRating.stars,
        comment: newRating.comment
      })
    });
    
    fetchRatings(); // Refresh ratings
    setNewRating({ stars: 5, comment: '' });
  };

  return (
    <div>
      <h3>Product Ratings</h3>
      
      {/* Rating form */}
      <form onSubmit={handleSubmit}>
        <select 
          value={newRating.stars} 
          onChange={(e) => setNewRating({...newRating, stars: parseInt(e.target.value)})}
        >
          {[1, 2, 3, 4, 5].map(n => <option key={n} value={n}>{n} stars</option>)}
        </select>
        <textarea 
          value={newRating.comment}
          onChange={(e) => setNewRating({...newRating, comment: e.target.value})}
          placeholder="Write your review..."
        />
        <button type="submit">Submit Rating</button>
      </form>

      {/* Display ratings */}
      <div>
        {ratings.map(rating => (
          <div key={rating.id}>
            <div>★ {rating.rateStars}/5</div>
            <p>{rating.comment}</p>
            <small>{new Date(rating.createdAt).toLocaleDateString()}</small>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ProductRatings;
```

---

## Testing with Postman

1. **Set up Authentication:**
   - First, login to get JWT token: POST `/api/auth/login`
   - Copy the JWT token from response
   - In Postman, go to Authorization tab → Type: Bearer Token → Paste token

2. **Test Create Rating:**
   - Method: POST
   - URL: `http://localhost:8080/api/ratings`
   - Body: Raw → JSON
   - Paste the JSON example from above

3. **Test Get Ratings:**
   - Method: GET
   - URL: `http://localhost:8080/api/ratings/product/{productId}`
   - Replace `{productId}` with actual product ID

---

## Security Notes

1. **Authentication Required:** All endpoints require a valid JWT token
2. **Authorization:** Consider implementing checks to ensure users can only:
   - Edit/delete their own ratings
   - Not rate the same product multiple times (optional)
3. **Validation:** The API validates that rating stars are between 1-5
4. **Rate Limiting:** Consider implementing rate limiting to prevent spam ratings

---

## Common Use Cases

1. **Display product average rating:**
   - Get all ratings for a product
   - Calculate average of `rateStars`

2. **Show user's rating history:**
   - Use `/api/ratings/user/{userId}` endpoint

3. **Allow users to edit their reviews:**
   - Get user's rating for a product
   - Use PUT endpoint to update

4. **Delete inappropriate reviews:**
   - Use DELETE endpoint (consider admin authorization)

---

## Database Indexes

The following indexes are automatically created:
- `userId` - for faster queries by user
- `productId` - for faster queries by product
- `email` (unique) in users collection
- Combined queries are also optimized

---

## Future Enhancements

Potential features to add:
- Pagination for large result sets
- Sorting options (by date, rating)
- Image uploads for reviews
- Helpful/unhelpful voting system
- Report inappropriate reviews
- Prevent duplicate ratings per user-product combination
- Average rating calculation endpoint

