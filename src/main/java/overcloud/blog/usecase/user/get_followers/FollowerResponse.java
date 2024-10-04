package overcloud.blog.usecase.user.get_followers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FollowerResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("image")
    private String image;

    @JsonProperty("username")
    private String username;
}
