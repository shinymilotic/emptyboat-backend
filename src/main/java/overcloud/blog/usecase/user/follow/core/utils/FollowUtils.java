package overcloud.blog.usecase.user.follow.core.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.entity.UserEntity;

import java.util.Set;

@Component
public class FollowUtils {
    public boolean isFollowing(UserEntity currentUser, UserEntity author) {
        Set<UserEntity> authorFollows = author.getFollowers();

        return currentUser != null && authorFollows.contains(currentUser);
    }

    public int getFollowingCount(UserEntity user) {
        return user.getFollowers().size();
    }
}
