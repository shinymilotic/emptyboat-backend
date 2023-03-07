package overcloud.blog.application.user.dto.update;

import overcloud.blog.application.user.dto.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

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
