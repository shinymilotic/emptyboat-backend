package overcloud.blog.usecase.auth.refresh_token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RefreshTokenRequest {
    @JsonProperty("refreshToken")
    private String refreshToken;
}
