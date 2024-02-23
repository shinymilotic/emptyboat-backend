package overcloud.blog.usecase.auth.get_followers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FollowerListResposne {
    @JsonProperty("followers")
    List<FollowerResponse> followers;
}
