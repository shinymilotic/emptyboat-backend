package overcloud.blog.usecase.user.core;

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
