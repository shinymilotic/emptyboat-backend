package overcloud.blog.domain.article;

import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "article_tag", schema = "public")
public class ArticleTag {

    @EmbeddedId
    private ArticleTagId id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @MapsId("tagId")
    private TagEntity tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @MapsId("articleId")
    private ArticleEntity article;

    public ArticleTag() {
    }

    public ArticleTagId getId() {
        return id;
    }

    public void setId(ArticleTagId id) {
        this.id = id;
    }

    public TagEntity getTag() {
        return tag;
    }

    public void setTag(TagEntity tag) {
        this.tag = tag;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }
}
