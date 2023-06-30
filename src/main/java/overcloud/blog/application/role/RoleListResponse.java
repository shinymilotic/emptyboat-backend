package overcloud.blog.application.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.role.core.RoleResponse;

import java.util.List;

@Getter
@Setter
@Builder
public class RoleListResponse {
    @JsonProperty("roles")
    private List<RoleResponse> roleList;
}
