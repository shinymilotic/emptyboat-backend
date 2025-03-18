package overcloud.blog.usecase.user.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.utils.validation.LetterAndNumber;

import static overcloud.blog.usecase.user.register.RegisterMsg.*;

@Builder
@Setter
@Getter
public class RegisterRequest {
    @JsonProperty("username")
    @NotBlank(message = USERNAME_NOTBLANK)
    @Size(min = 6, max = 32, message = USERNAME_SIZE)
    @LetterAndNumber(message = USERNAME_LETTERANDNUMBER)
    private String username;

    @JsonProperty("email")
    @NotBlank(message = EMAIL_NOTBLANK)
    @Email(message = EMAIL_VALID)
    private String email;

    @JsonProperty("password")
    @NotBlank(message = PASSWORD_NOTBLANK)
    @Size(min = 8, max = 64, message = PASSWORD_SIZE)
    private String password;
}
