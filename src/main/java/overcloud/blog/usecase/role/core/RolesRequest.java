package overcloud.blog.usecase.role.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.role.core.RoleDto;

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
