package overcloud.blog.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagId implements Serializable {
    @Column(name = "article_id")
    private UUID articleId;

    @Column(name = "tag_id")
    private UUID tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleTagId that = (ArticleTagId) o;

        if (!articleId.equals(that.articleId)) return false;
        return tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        int result = articleId.hashCode();
        result = 31 * result + tagId.hashCode();
        return result;
    }
}
