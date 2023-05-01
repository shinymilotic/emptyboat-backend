package overcloud.blog.domain.user.follow;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.domain.article.FollowId;
import overcloud.blog.domain.user.UserEntity;

@Entity
@Getter
@Setter
@Table(name = "follows", schema = "public")
public class FollowEntity {
    @EmbeddedId
    private FollowId id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @MapsId("followerId")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "followee_id")
    @MapsId("followeeId")
    private UserEntity followee;

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
