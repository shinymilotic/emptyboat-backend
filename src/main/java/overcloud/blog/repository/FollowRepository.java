package overcloud.blog.repository;

import overcloud.blog.entity.FollowEntity;
import java.util.UUID;

public interface FollowRepository {
    void unfollow(String currentUsername, String followingUsername);
    void save(FollowEntity followEntity);
    void delete(FollowEntity followEntity);
    void deleteByUserId(UUID userId);
}
