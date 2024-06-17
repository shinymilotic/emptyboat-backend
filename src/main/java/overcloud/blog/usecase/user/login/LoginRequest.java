package overcloud.blog.usecase.user.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class LoginRequest {
    @JsonProperty("email")
    @NotNull(message = "user.login.email.not-null")
    @Size(min = 3, max = 256, message = "user.login.email.size")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "user.login.password.not-null")
    private String password;
}
