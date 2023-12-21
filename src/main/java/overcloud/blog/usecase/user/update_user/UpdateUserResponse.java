package overcloud.blog.usecase.user.update_user;

import com.fasterxml.jackson.annotation.JsonProperty;

import overcloud.blog.usecase.user.core.UserResponse;

public class UpdateUserResponse{
    @JsonProperty("user")
    private UserResponse userResponse;

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }
}
