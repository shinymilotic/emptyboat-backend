package overcloud.blog.usecase.user.get_followers;

import java.util.List;
import java.util.UUID;
import overcloud.blog.usecase.user.common.UserResponse;

public interface GetFollowers {
    List<UserResponse> getFollowers(UUID userId);
}
