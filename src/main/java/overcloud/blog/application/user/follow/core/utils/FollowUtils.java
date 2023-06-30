package overcloud.blog.application.user.follow.core.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.application.user.core.UserEntity;

import java.util.Set;

@Component
public class FollowUtils {
    public boolean isFollowing(UserEntity currentUser, UserEntity author) {
        Set<UserEntity> authorFollows = author.getFollowers();

        if(currentUser != null && authorFollows.contains(currentUser)) {
            return true;
        }

        return false;
    }

    public int getFollowingCount(UserEntity user) {
        return user.getFollowers().size();
    }
}
