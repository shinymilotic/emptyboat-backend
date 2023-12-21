package overcloud.blog.usecase.user.register;

import com.fasterxml.jackson.annotation.JsonProperty;

import overcloud.blog.usecase.user.core.UserResponse;

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
