package overcloud.blog.application.user.dto.register;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import overcloud.blog.infrastructure.validation.LetterAndNumber;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class RegisterRequest {
    @JsonProperty("username")
    @NotNull
    @Size(min = 6, max = 32, message = "Username must be between 6 and 32 characters long.")
    @LetterAndNumber(message = "Username must contains only letter and number")
    private String username;
    @JsonProperty("email")
    @NotNull
    @Email
    @Size(min = 3, max = 320, message = "Please enter a valid email address")
    private String email;

    @JsonProperty("password")
    @NotNull
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters long.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
