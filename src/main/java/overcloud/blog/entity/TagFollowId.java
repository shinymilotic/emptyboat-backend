package overcloud.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagFollowId implements Serializable {
    @Column(name = "follower_id")
    private UUID followerId;

    @Column(name = "tag_id")
    private UUID tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagFollowId that = (TagFollowId) o;

        if (!followerId.equals(that.followerId)) return false;
        return tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        int result = followerId.hashCode();
        result = 31 * result + tagId.hashCode();
        return result;
    }
}
