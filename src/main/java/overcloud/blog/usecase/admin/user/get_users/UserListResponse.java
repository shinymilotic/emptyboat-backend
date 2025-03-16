package overcloud.blog.usecase.admin.user.get_users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overcloud.blog.usecase.user.common.UserResponse;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    private List<UserResponse> users;
    private Integer userCount;
}
