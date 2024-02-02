package overcloud.blog.usecase.auth.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import overcloud.blog.usecase.auth.common.UserResponse;

public class RegisterResponse {
    @JsonProperty("user")
    private UserResponse userResponse;

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }
}
