package overcloud.blog.domain.article;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ArticleTagId implements Serializable {

    @Column(name = "article_id")
    private UUID articleId;

    @Column(name = "tag_id")
    private UUID tagId;

    public ArticleTagId() {
    }

    public ArticleTagId(UUID articleId, UUID tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }

    public UUID getArticleId() {
        return articleId;
    }

    public void setArticleId(UUID articleId) {
        this.articleId = articleId;
    }

    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(UUID tagId) {
        this.tagId = tagId;
    }
}
