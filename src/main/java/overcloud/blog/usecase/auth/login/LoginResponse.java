package overcloud.blog.usecase.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import overcloud.blog.usecase.auth.common.UserResponse;

@NoArgsConstructor
public class LoginResponse {

    @JsonProperty("user")
    private UserResponse userResponse;

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }
}
