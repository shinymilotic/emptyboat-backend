package overcloud.blog.application.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.role.core.RoleDto;

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
