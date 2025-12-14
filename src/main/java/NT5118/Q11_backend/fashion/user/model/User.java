package NT5118.Q11_backend.fashion.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "users")
@Schema(description = "User entity")
public class User {
    @Schema(description = "User ID", example = "507f1f77bcf86cd799439011")
    @Id
    private String id;

    @Schema(description = "Email address", example = "john@example.com")
    @Indexed(unique = true)
    private String email;

    @Schema(description = "Username", example = "john_doe")
    @Indexed(unique = true)
    private String username;

    @Schema(description = "Hashed password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password; // hashed

    @Indexed(unique = true)
    private String phoneNumber = "0";

    private String userAddress;


    // constructors, getters, setters
    public User() {}
    public User(String username, String email, String password, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password;}

    public String getPhoneNumber() { return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getUserAddress() { return userAddress; }
    public void setUserAddress(String userAddress) { this.userAddress = userAddress; }

}
