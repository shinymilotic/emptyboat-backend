package overcloud.blog.application.user.get_current_user;

import overcloud.blog.application.user.core.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrentUserResponse {
    @JsonProperty("user")
    private UserResponse userResponse;

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }
}
