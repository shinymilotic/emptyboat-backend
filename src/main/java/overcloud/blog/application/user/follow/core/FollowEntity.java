package overcloud.blog.application.user.follow.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.entity.UserEntity;

@Entity
@Getter
@Setter
@Table(name = "follows", schema = "public")
public class FollowEntity {
    @EmbeddedId
    private FollowId id;

    @ManyToOne
    @MapsId("followerId")
    private UserEntity follower;

    @ManyToOne
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
