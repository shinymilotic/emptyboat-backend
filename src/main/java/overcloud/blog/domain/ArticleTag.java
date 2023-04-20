package overcloud.blog.domain;

import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "article_tag", schema = "public")
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagEntity tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    public ArticleTag() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
