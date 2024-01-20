package overcloud.blog.usecase.auth.assign_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.auth.common.RolesRequest;

@Getter
@Setter
@Builder
public class RoleAssignmentRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private RolesRequest roles;
}
