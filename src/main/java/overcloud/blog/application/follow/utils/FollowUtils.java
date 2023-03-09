package overcloud.blog.application.follow.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.domain.user.follow.FollowEntity;

import java.util.Set;

@Component
public class FollowUtils {
    public boolean isFollowing(UserEntity currentUser, UserEntity author) {
        Set<FollowEntity> follows = currentUser.getFollowee();
        Set<FollowEntity> authorFollows = author.getFollower();
        for (FollowEntity follow: follows) {
            if(authorFollows.contains(follow)) {
                return true;
            }
        }

        return false;
    }
}
