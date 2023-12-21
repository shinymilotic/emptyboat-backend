package overcloud.blog.usecase.user_role.assign_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.role.RolesRequest;

@Getter
@Setter
@Builder
public class RoleAssignmentRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private RolesRequest roles;
}
