package overcloud.blog.repository.jparepository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import overcloud.blog.usecase.user.follow.core.FollowEntity;
import overcloud.blog.usecase.user.follow.core.FollowId;

import java.util.List;

@Repository
public interface JpaFollowRepository extends JpaRepository<FollowEntity, FollowId> {

    @Query("""
             SELECT f FROM FollowEntity f WHERE\
             f.follower.username = :currentUsername\
             AND f.followee.username = :followingUsername \
            """)
    List<FollowEntity> getFollowing(String currentUsername, String followingUsername);
}
