package overcloud.blog.usecase.user.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import overcloud.blog.usecase.user.common.UserResMsg;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginRequest {
    @JsonProperty("email")
    @NotNull(message = UserResMsg.USER_LOGIN_PASSWORD_NOTNULL)
    @Size(min = 3, max = 256, message = UserResMsg.USER_LOGIN_EMAIL_SIZE)
    private String email;

    @JsonProperty("password")
    @NotNull(message = UserResMsg.USER_LOGIN_PASSWORD_NOTNULL)
    private String password;
}
