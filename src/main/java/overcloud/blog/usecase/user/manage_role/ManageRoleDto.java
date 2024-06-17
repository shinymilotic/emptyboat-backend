package overcloud.blog.usecase.user.manage_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManageRoleDto {
    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("updateRoleName")
    private String updateRoleName;

    @JsonProperty("updateFlg")
    private int updateFlg;
}
