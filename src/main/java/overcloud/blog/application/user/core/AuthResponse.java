package overcloud.blog.application.user.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AuthResponse {
    @JsonProperty("email")
    private String email;

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("username")
    private String username;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("image")
    private String image;
}
