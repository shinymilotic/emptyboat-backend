package overcloud.blog.application.user.register;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import overcloud.blog.infrastructure.validation.annotation.LetterAndNumber;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class RegisterRequest {

    @JsonProperty("username")
    @NotBlank(message = "user.register.username.not-blank")
    @Size(min = 6, max = 32, message = "user.register.username.size")
    @LetterAndNumber(message = "user.register.username.letter-and-number")
    private String username;

    @JsonProperty("email")
    @NotBlank(message = "user.register.email.not-blank")
    @Email(message = "user.register.email.valid")
    @Size(min = 3, max = 320, message = "user.register.email.size")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "user.register.password.not-blank")
    @Size(min = 8, max = 64, message = "user.register.password.size")
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
