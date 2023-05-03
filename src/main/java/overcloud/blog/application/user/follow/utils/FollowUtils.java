package overcloud.blog.application.user.follow.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.application.user.UserEntity;

import java.util.Set;

@Component
public class FollowUtils {
    public boolean isFollowing(UserEntity currentUser, UserEntity author) {
        Set<UserEntity> authorFollows = author.getFollower();

        if(authorFollows.contains(currentUser)) {
            return true;
        }

        return false;
    }

    public int getFollowingCount(UserEntity user) {
        return user.getFollower().size();
    }
}
