package overcloud.blog.usecase.user.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RoleListResponse {
    @JsonProperty("roles")
    private List<RoleResponse> roleList;
}
