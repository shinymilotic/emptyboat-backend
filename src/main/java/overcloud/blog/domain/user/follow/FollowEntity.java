package overcloud.blog.domain.user.follow;

import jakarta.persistence.*;
import overcloud.blog.domain.user.UserEntity;

@Entity
@Table(name = "follows", schema = "public")
public class FollowEntity {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserEntity follower;
    @ManyToOne
    @JoinColumn(name = "followee_id")
    private UserEntity followee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getFollower() {
        return follower;
    }

    public void setFollower(UserEntity follower) {
        this.follower = follower;
    }

    public UserEntity getFollowee() {
        return followee;
    }

    public void setFollowee(UserEntity follwing) {
        this.followee = follwing;
    }
}
