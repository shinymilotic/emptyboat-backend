package overcloud.blog.application.role.manage_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManageRoleDto {
    @JsonProperty("currentRoleName")
    private String currentRoleName;

    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("updateFlg")
    private int updateFlg;
}
