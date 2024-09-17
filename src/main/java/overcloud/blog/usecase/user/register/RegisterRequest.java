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
    @NotBlank(message = UserResMsg.USER_REGISTER_USERNAME_NOTBLANK)
    @Size(min = 6, max = 32, message = UserResMsg.USER_REGISTER_USERNAME_SIZE)
    @LetterAndNumber(message = UserResMsg.USER_REGISTER_USERNAME_LETTERANDNUMBER)
    private String username;

    @JsonProperty("email")
    @NotBlank(message = UserResMsg.USER_REGISTER_EMAIL_NOTBLANK)
    @Email(message = UserResMsg.USER_REGISTER_EMAIL_VALID)
    private String email;

    @JsonProperty("password")
    @NotBlank(message = UserResMsg.USER_REGISTER_PASSWORD_NOTBLANK)
    @Size(min = 8, max = 64, message = UserResMsg.USER_REGISTER_PASSWORD_SIZE)
    private String password;
}
