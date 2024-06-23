package overcloud.blog.repository;

import java.util.List;
import overcloud.blog.usecase.user.follow.core.FollowEntity;

public interface IFollowRepository {
    List<FollowEntity> getFollowing(String currentUsername, String followingUsername);
    void save(FollowEntity followEntity);
    void delete(FollowEntity followEntity);
}
