package overcloud.blog.domain;

import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "article_tag", schema = "public")
public class ArticleTag {
    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private TagEntity tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private ArticleEntity article;

    public ArticleTag() {
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
