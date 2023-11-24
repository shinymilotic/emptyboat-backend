package overcloud.blog.application.user.follow.core.repository;


import overcloud.blog.application.user.follow.core.FollowId;
import overcloud.blog.application.user.follow.core.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, FollowId> {

    @Query("""
             SELECT f FROM FollowEntity f WHERE\
             f.follower.username = :currentUsername\
             AND f.followee.username = :followingUsername \
            """)
    List<FollowEntity> getFollowing(String currentUsername, String followingUsername);
}
