package overcloud.blog.usecase.user.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RolesRequest {
    @JsonProperty("roles")
    @NotBlank(message = "role.roles.not-blank")
    private List<RoleDto> roles;

    public RolesRequest() {
    }
}
