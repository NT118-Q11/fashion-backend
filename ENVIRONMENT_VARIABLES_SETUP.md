# Environment Variables Setup Guide

## Quick Setup for OAuth Error Fix

The error "401: invalid_client" occurs because Google OAuth credentials are not configured. Follow these steps:

## Step 1: Get Google OAuth Credentials

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable **Google+ API** (or Google People API)
4. Go to **APIs & Services** > **Credentials**
5. Click **Create Credentials** > **OAuth 2.0 Client IDs**
6. Configure:
   - **Application type**: Web application
   - **Name**: Fashion Backend
   - **Authorized JavaScript origins**: 
     - `http://localhost:8080`
   - **Authorized redirect URIs**:
     - `http://localhost:8080/login/oauth2/code/google`
     - `http://localhost:8080/api/auth/oauth2/callback/google`
7. Click **Create** and copy your **Client ID** and **Client Secret**

## Step 2: Set Environment Variables

### Windows PowerShell (Temporary - Current Session Only)
```powershell
$env:GOOGLE_CLIENT_ID="your-actual-client-id-here"
$env:GOOGLE_CLIENT_SECRET="your-actual-client-secret-here"
$env:JWT_SECRET="your-secure-jwt-secret-minimum-32-characters"
```

### Windows PowerShell (Permanent - User Level)
```powershell
[System.Environment]::SetEnvironmentVariable('GOOGLE_CLIENT_ID', 'your-actual-client-id-here', 'User')
[System.Environment]::SetEnvironmentVariable('GOOGLE_CLIENT_SECRET', 'your-actual-client-secret-here', 'User')
[System.Environment]::SetEnvironmentVariable('JWT_SECRET', 'your-secure-jwt-secret-minimum-32-characters', 'User')
```

### IntelliJ IDEA Run Configuration (Recommended for Development)
1. Go to **Run** > **Edit Configurations**
2. Select your Spring Boot application
3. Add to **Environment Variables**:
   ```
   GOOGLE_CLIENT_ID=your-actual-client-id-here;GOOGLE_CLIENT_SECRET=your-actual-client-secret-here;JWT_SECRET=your-secure-jwt-secret
   ```

### Docker Compose (.env file)
Create a `.env` file in the project root:
```env
GOOGLE_CLIENT_ID=your-actual-client-id-here
GOOGLE_CLIENT_SECRET=your-actual-client-secret-here
JWT_SECRET=your-secure-jwt-secret-minimum-32-characters
SPRING_DATA_MONGODB_URI=mongodb://admin:admin123@mongodb:27017/fashion-mongodb?authSource=admin
```

## Step 3: Restart Your Application

After setting the environment variables, restart your Spring Boot application for the changes to take effect.

## Step 4: Test OAuth

1. Navigate to: `http://localhost:8080/api/auth/oauth2/google`
2. You should be redirected to Google's login page
3. After authentication, you'll be redirected back to your application

## Verification

Check if environment variables are set:
```powershell
# PowerShell
echo $env:GOOGLE_CLIENT_ID
echo $env:GOOGLE_CLIENT_SECRET
```

## Troubleshooting

### Still getting "401: invalid_client"?
- Verify the Client ID and Client Secret are correct
- Check that the redirect URIs in Google Cloud Console match exactly:
  - `http://localhost:8080/login/oauth2/code/google`
  - `http://localhost:8080/api/auth/oauth2/callback/google`
- Make sure there are no extra spaces in your environment variables
- Restart your application after setting environment variables

### "redirect_uri_mismatch" error?
- Add the exact redirect URI from the error message to your Google Cloud Console Authorized Redirect URIs

### OAuth consent screen not configured?
- Go to **APIs & Services** > **OAuth consent screen** in Google Cloud Console
- Configure it (can use Internal for testing, or External for public)
- Add test users if using Internal mode

## Alternative: Direct Configuration (NOT Recommended for Production)

If you just want to test quickly, you can temporarily add values directly to `application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=your-actual-client-id-here
spring.security.oauth2.client.registration.google.client-secret=your-actual-client-secret-here
```

⚠️ **WARNING**: Never commit real credentials to version control! Add `application-local.properties` to `.gitignore` if using this approach.

## Security Best Practices

1. ✅ Use environment variables for sensitive data
2. ✅ Never commit credentials to Git
3. ✅ Use different credentials for dev/staging/production
4. ✅ Rotate secrets regularly
5. ✅ Use strong, random JWT secrets (minimum 32 characters)

## Example JWT Secret Generation

```powershell
# Generate a random base64 string for JWT secret
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

