package overcloud.blog.application.user.refresh_token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
