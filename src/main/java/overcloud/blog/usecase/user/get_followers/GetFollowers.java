package overcloud.blog.usecase.user.get_followers;

import java.util.UUID;

public interface GetFollowers {
    FollowerListResposne getFollowers(UUID userId);
}
