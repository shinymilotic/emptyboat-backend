package overcloud.blog.usecase.role.manage_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManageRoleResponse {
    @JsonProperty("rowAffected")
    private int rowAffected;
}
