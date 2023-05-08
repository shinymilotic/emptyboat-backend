package overcloud.blog.application.user.follow.core.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.application.user.core.UserEntity;

import java.util.Optional;
import java.util.Set;

@Component
public class FollowUtils {
    public boolean isFollowing(Optional<UserEntity> currentUser, UserEntity author) {
        Set<UserEntity> authorFollows = author.getFollower();

        if(currentUser.isPresent() && authorFollows.contains(currentUser)) {
            return true;
        }

        return false;
    }

    public int getFollowingCount(UserEntity user) {
        return user.getFollower().size();
    }
}
