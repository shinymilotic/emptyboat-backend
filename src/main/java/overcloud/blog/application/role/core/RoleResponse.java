package overcloud.blog.application.role.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleResponse {
    @JsonProperty("roleId")
    private String roleId;

    @JsonProperty("roleName")
    private String roleName;
}
