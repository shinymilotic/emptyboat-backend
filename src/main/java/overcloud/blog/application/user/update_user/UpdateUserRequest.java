package overcloud.blog.application.user.update_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import overcloud.blog.infrastructure.validation.annotation.LetterAndNumber;

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateUserRequest {
    @JsonProperty("username")
    @NotBlank(message = "user.register.username.not-blank")
    @Size(min = 6, max = 32, message = "user.register.username.size")
    @LetterAndNumber(message = "user.register.username.letter-and-number")
    private String username;

    @JsonProperty("email")
    @NotBlank(message = "user.register.email.not-blank")
    @Email(message = "user.register.email.valid")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "user.register.password.not-blank")
    @Size(min = 8, max = 64, message = "user.register.password.size")
    private String password;

    @JsonProperty("bio")
    @Size(min = 8, max = 1000, message = "user.profile.bio.size")
    private String bio;

    @JsonProperty("image")
    private String image;

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
