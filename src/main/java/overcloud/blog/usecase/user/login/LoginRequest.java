package overcloud.blog.usecase.user.login;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Size(min = 3, max = 256, message = "user.login.email.size")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "user.login.password.not-null")
    private String password;
}
