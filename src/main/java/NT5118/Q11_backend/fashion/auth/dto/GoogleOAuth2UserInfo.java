package NT5118.Q11_backend.fashion.auth.dto;

public class GoogleOAuth2UserInfo {
    private String id;
    private String email;
    private String name;
    private String picture;
    private String accessToken; // ID token từ Google Sign-In SDK

    public GoogleOAuth2UserInfo() {}

    public GoogleOAuth2UserInfo(String id, String email, String name, String picture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public GoogleOAuth2UserInfo(String id, String email, String name, String picture, String accessToken) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.accessToken = accessToken;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
