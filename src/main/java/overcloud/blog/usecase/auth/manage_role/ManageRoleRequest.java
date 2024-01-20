package overcloud.blog.usecase.auth.manage_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManageRoleRequest {
    @JsonProperty("roles")
    @NotBlank(message = "role.roles.not-blank")
    private List<ManageRoleDto> roles;
}
