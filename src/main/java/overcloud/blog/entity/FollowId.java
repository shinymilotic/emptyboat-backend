package overcloud.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class FollowId implements Serializable {
    @Column(name = "follower_id")
    private UUID followerId;

    @Column(name = "followee_id")
    private UUID followeeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FollowId followId = (FollowId) o;

        if (!followerId.equals(followId.followerId)) return false;
        return followeeId.equals(followId.followeeId);
    }

    @Override
    public int hashCode() {
        int result = followerId.hashCode();
        result = 31 * result + followeeId.hashCode();
        return result;
    }
}
