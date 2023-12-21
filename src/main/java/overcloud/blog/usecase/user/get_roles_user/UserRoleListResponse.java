package overcloud.blog.usecase.user.get_roles_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserRoleListResponse {
    @JsonProperty("roles")
    private List<UserRoleResponse> userRoleResponseList;
}
