package overcloud.blog.usecase.user.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import overcloud.blog.usecase.user.common.UserResMsg;

import static overcloud.blog.usecase.user.login.LoginResMsg.USER_LOGIN_EMAIL_SIZE;
import static overcloud.blog.usecase.user.login.LoginResMsg.USER_LOGIN_PASSWORD_NOTNULL;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginRequest {
    @JsonProperty("email")
    @Size(min = 3, max = 256, message = USER_LOGIN_EMAIL_SIZE)
    private String email;

    @JsonProperty("password")
    @NotNull(message = USER_LOGIN_PASSWORD_NOTNULL)
    private String password;
}
