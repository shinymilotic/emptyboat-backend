package overcloud.blog.application.user.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateUserRequest {
    @JsonProperty("email")
    @Email
    @Size(min = 3, max = 320, message = "Please enter a valid email address")
    private String email;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("image")
    private String image;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
