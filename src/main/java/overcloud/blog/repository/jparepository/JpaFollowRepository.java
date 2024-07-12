package overcloud.blog.repository.jparepository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.usecase.user.follow.core.FollowEntity;
import overcloud.blog.usecase.user.follow.core.FollowId;

@Repository
public interface JpaFollowRepository extends JpaRepository<FollowEntity, FollowId> {

    @Modifying
    @Query("""
             DELETE FROM FollowEntity f WHERE\
             f.follower.username = :currentUsername\
             AND f.followee.username = :followingUsername \
            """)
    void unfollow(String currentUsername, String followingUsername);
}
