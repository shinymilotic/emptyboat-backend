package overcloud.blog.usecase.user.refresh_token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RefreshTokenResponse {
    @JsonProperty("userId")
    private String userId;
}
