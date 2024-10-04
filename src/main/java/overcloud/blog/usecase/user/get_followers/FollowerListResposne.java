package overcloud.blog.usecase.user.get_followers;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.user.common.UserResponse;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FollowerListResposne {
    @JsonProperty("followers")
    List<UserResponse> followers;
    
    public FollowerListResposne() {
        followers = new ArrayList<>();
    }
}
