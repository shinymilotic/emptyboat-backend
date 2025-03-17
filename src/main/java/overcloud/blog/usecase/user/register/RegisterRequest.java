package overcloud.blog.usecase.user.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.utils.validation.LetterAndNumber;
import overcloud.blog.usecase.user.common.UserResMsg;

@Builder
@Setter
@Getter
public class RegisterRequest {
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
}
