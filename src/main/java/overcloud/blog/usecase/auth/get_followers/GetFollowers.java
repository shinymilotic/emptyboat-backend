package overcloud.blog.usecase.auth.get_followers;

import java.util.UUID;

public interface GetFollowers {
    FollowerListResposne getFollowers(UUID userId);
}
