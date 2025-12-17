# Quick Start Guide - Testing Rating API

## Prerequisites
- MongoDB running on `localhost:27017`
- Fashion Backend application running on `http://localhost:8080`

## Start the Application

```powershell
# Navigate to project directory
cd C:\Users\tung\IdeaProjects\fashion-backend

# Build the project
.\gradlew.bat clean build -x test

# Run the application
.\gradlew.bat bootRun
```

Or use the start script:
```powershell
.\start.ps1
```

## Quick Test with cURL (PowerShell)

### 1. Create a Rating
```powershell
$body = @{
    userId = "test-user-123"
    productId = "test-product-456"
    rateStars = 5
    comment = "Excellent product! Highly recommended."
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/ratings" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### 2. Get All Ratings
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ratings" -Method GET
```

### 3. Get Ratings by Product ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/product/test-product-456" -Method GET
```

### 4. Get Ratings by User ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/user/test-user-123" -Method GET
```

### 5. Update a Rating (replace {rating-id} with actual ID)
```powershell
$body = @{
    userId = "test-user-123"
    productId = "test-product-456"
    rateStars = 4
    comment = "Updated: Still good but found minor issues."
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/{rating-id}" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $body
```

### 6. Delete a Rating (replace {rating-id} with actual ID)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/{rating-id}" -Method DELETE
```

## Testing with Postman

### Import Collection
1. Open Postman
2. Create a new collection: "Fashion Backend - Ratings"
3. Add the following requests:

#### POST - Create Rating
- **URL**: `http://localhost:8080/api/ratings`
- **Method**: POST
- **Body** (raw JSON):
```json
{
  "userId": "user123",
  "productId": "product456",
  "rateStars": 5,
  "comment": "Excellent product!"
}
```

#### GET - All Ratings
- **URL**: `http://localhost:8080/api/ratings`
- **Method**: GET

#### GET - Product Ratings
- **URL**: `http://localhost:8080/api/ratings/product/product456`
- **Method**: GET

#### GET - User Ratings
- **URL**: `http://localhost:8080/api/ratings/user/user123`
- **Method**: GET

#### PUT - Update Rating
- **URL**: `http://localhost:8080/api/ratings/{rating-id}`
- **Method**: PUT
- **Body** (raw JSON):
```json
{
  "userId": "user123",
  "productId": "product456",
  "rateStars": 4,
  "comment": "Updated review"
}
```

#### DELETE - Delete Rating
- **URL**: `http://localhost:8080/api/ratings/{rating-id}`
- **Method**: DELETE

## Check MongoDB Data

```powershell
# Connect to MongoDB shell
mongosh

# Use the database
use fashion_mongodb

# View all ratings
db.ratings.find().pretty()

# Count ratings
db.ratings.countDocuments()

# Find ratings by product
db.ratings.find({ productId: "test-product-456" }).pretty()

# Find ratings by user
db.ratings.find({ userId: "test-user-123" }).pretty()
```

## Common Issues & Solutions

### Issue 1: MongoDB Connection Error
**Error**: `Unable to connect to MongoDB`
**Solution**: 
```powershell
# Start MongoDB service
net start MongoDB
# Or if running in Docker
docker start mongodb
```

### Issue 2: Port Already in Use
**Error**: `Port 8080 is already in use`
**Solution**:
```powershell
# Find process using port 8080
netstat -ano | findstr :8080
# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### Issue 3: Validation Error
**Error**: `Rating must be at least 1 star`
**Solution**: Ensure `rateStars` is between 1 and 5

### Issue 4: 404 Not Found
**Error**: `Rating not found`
**Solution**: Use a valid rating ID from the database

## Sample Test Scenario

```powershell
# 1. Create first rating
$rating1 = @{
    userId = "user001"
    productId = "prod001"
    rateStars = 5
    comment = "Amazing quality! Will buy again."
} | ConvertTo-Json

$response1 = Invoke-RestMethod -Uri "http://localhost:8080/api/ratings" `
    -Method POST `
    -ContentType "application/json" `
    -Body $rating1

Write-Host "Created Rating ID: $($response1.id)"

# 2. Create second rating for same product
$rating2 = @{
    userId = "user002"
    productId = "prod001"
    rateStars = 4
    comment = "Good product, fast delivery."
} | ConvertTo-Json

$response2 = Invoke-RestMethod -Uri "http://localhost:8080/api/ratings" `
    -Method POST `
    -ContentType "application/json" `
    -Body $rating2

# 3. Get all ratings for the product
$productRatings = Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/product/prod001" -Method GET
Write-Host "Total ratings for product: $($productRatings.Count)"
Write-Host "Average rating: $(($productRatings | Measure-Object -Property rateStars -Average).Average)"

# 4. Update first rating
$updateRating = @{
    userId = "user001"
    productId = "prod001"
    rateStars = 4
    comment = "Updated: Still good after using for a month."
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/$($response1.id)" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $updateRating

# 5. Get updated rating
$updated = Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/$($response1.id)" -Method GET
Write-Host "Updated rating: $($updated.rateStars) stars"
Write-Host "Updated comment: $($updated.comment)"

# 6. Clean up - delete ratings
Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/$($response1.id)" -Method DELETE
Invoke-RestMethod -Uri "http://localhost:8080/api/ratings/$($response2.id)" -Method DELETE
Write-Host "Cleanup completed"
```

## Next Steps

1. **Add Authentication**: Uncomment authentication requirements in SecurityConfig.java
2. **Add Authorization**: Ensure users can only edit/delete their own ratings
3. **Add Pagination**: Implement pagination for large result sets
4. **Calculate Statistics**: Add endpoint to get average rating per product
5. **Prevent Duplicates**: Add validation to prevent users from rating the same product multiple times

## API Documentation

For complete API documentation, see: `RATING_API_GUIDE.md`

