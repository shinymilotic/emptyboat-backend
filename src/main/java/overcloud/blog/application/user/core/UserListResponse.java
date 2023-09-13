package overcloud.blog.application.user.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class UserListResponse {
    private List<UserResponse> users;
}
