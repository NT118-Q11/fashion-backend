package NT5118.Q11_backend.fashion.auth.dto;

public class GoogleOAuth2UserInfo {
    private String id;
    private String email;
    private String name;
    private String picture;

    public GoogleOAuth2UserInfo() {}

    public GoogleOAuth2UserInfo(String id, String email, String name, String picture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
}

