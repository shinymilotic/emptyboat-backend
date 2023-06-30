package overcloud.blog.application.role.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("updateFlg")
    private int updateFlg;
}
