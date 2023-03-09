package overcloud.blog.domain.user.follow;

import jakarta.persistence.*;
import overcloud.blog.domain.user.UserEntity;

import java.util.UUID;

@Entity
@Table(name = "follows", schema = "public")
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserEntity follower;
    @ManyToOne
    @JoinColumn(name = "followee_id")
    private UserEntity followee;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FollowEntity that = (FollowEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
