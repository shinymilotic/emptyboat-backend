package overcloud.blog.repository;

import overcloud.blog.usecase.user.follow.core.FollowEntity;

public interface IFollowRepository {
    void unfollow(String currentUsername, String followingUsername);
    void save(FollowEntity followEntity);
    void delete(FollowEntity followEntity);
}
