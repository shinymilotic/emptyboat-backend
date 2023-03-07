package overcloud.blog.application.follow.repository;


import overcloud.blog.domain.user.follow.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    @Query(" SELECT FollowEntity FROM FollowEntity f WHERE" +
            " f.follower.username = :currentUsername" +
            " AND f.followee.username = :followingUsername ")
    FollowEntity getFollowing(String currentUsername, String followingUsername);
}
