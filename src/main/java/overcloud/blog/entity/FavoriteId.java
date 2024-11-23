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
public class FavoriteId implements Serializable {
    @Column(name = "article_id")
    private UUID articleId;

    @Column(name = "user_id")
    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoriteId that = (FavoriteId) o;

        if (!articleId.equals(that.articleId)) return false;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = articleId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
